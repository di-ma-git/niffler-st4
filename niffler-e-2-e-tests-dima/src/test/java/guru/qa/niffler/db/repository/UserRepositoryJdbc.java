package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

    private static final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.AUTH);
    private static final DataSource udDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA);
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    @Override
    public UserAuthEntity createInAuth(UserAuthEntity user) {
        try (Connection connection = authDs.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement userPs = connection.prepareStatement(
                    "INSERT INTO \"user\" " +
                            "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement authorityPs = connection.prepareStatement(
                         "INSERT INTO \"authority\" (user_id, authority) values (?, ?)")) {

                userPs.setString(1, user.getUsername());
                userPs.setString(2, passwordEncoder.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());

                userPs.executeUpdate();

                UUID authUserId;
                try (ResultSet keys = userPs.getGeneratedKeys()) {
                    if (keys.next()) {
                        authUserId = UUID.fromString(keys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can't find id");
                    }
                }

                for (Authority authority : Authority.values()) {
                    authorityPs.setObject(1, authUserId);
                    authorityPs.setString(2, authority.name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();

                connection.commit();
                user.setId(authUserId);
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public UserEntity createInUserdata(UserEntity user) {
        try (Connection connection = udDs.getConnection()) {

            try (PreparedStatement udPs = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, currency) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

                udPs.setString(1, user.getUsername());
                udPs.setString(2, user.getCurrency().name());
                udPs.executeUpdate();

                try (ResultSet keys = udPs.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setId(UUID.fromString(keys.getString("id")));
                    } else {
                        throw new IllegalStateException("Can't find id");
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public Optional<UserAuthEntity> findInAuthById(UUID id) {
        try (Connection connection = authDs.getConnection()) {
            try (PreparedStatement userPS = connection.prepareStatement("SELECT * FROM \"user\" AS u " +
                    "JOIN \"authority\" AS a " +
                    "ON u.id = a.user_id " +
                    "WHERE u.id = ?")) {

                userPS.setObject(1, id);

                userPS.execute();
                UserAuthEntity user = null;
                ResultSet resultSet = userPS.getResultSet();
                boolean isProcessed = false;
                while (resultSet.next()){
                    if (!isProcessed) {
                        user = new UserAuthEntity();
                        user.setId(resultSet.getObject(1, UUID.class));
                        user.setUsername(resultSet.getString(2));
                        user.setPassword(resultSet.getString(3));
                        user.setEnabled(resultSet.getBoolean(4));
                        user.setAccountNonExpired(resultSet.getBoolean(5));
                        user.setAccountNonLocked(resultSet.getBoolean(6));
                        user.setCredentialsNonExpired(resultSet.getBoolean(7));
                        isProcessed = true;
                    }

                    if (resultSet.getString(10) != null){
                        AuthorityEntity authority = new AuthorityEntity();
                        authority.setId(resultSet.getObject(8, UUID.class));
                        authority.setAuthority(Authority.valueOf(resultSet.getString(10)));
                        user.getAuthorities().add(authority);
                    }
                }
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findInUserdataById(UUID id) {
        try (Connection connection = udDs.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"user\" WHERE id = ?")) {

                ps.setObject(1, id);

                ps.execute();
                ResultSet resultSet = ps.getResultSet();
                UserEntity user = null;
                while (resultSet.next()) {
                    user = new UserEntity();
                    user.setId(resultSet.getObject("id", UUID.class));
                    user.setUsername(resultSet.getString("username"));
                    user.setCurrency(resultSet.getObject("currency", CurrencyValues.class));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setPhoto(resultSet.getBytes("photo"));
                }
                return Optional.ofNullable(user);
            } catch (Exception e) {
                throw new IllegalStateException("Can't find id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateInAuth(UserAuthEntity user) {
        try (Connection connection = authDs.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement userPs = connection.prepareStatement("UPDATE \"user\" " +
                    "SET username = ?, password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?," +
                    "credentials_non_expired = ? WHERE id = ?");
                 PreparedStatement authDelPs = connection.prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?");
                 PreparedStatement authPs = connection.prepareStatement("INSERT INTO \"authority\" (user_id, authority) values (?, ?)")) {

                userPs.setString(1, user.getUsername());
                userPs.setString(2, passwordEncoder.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());
                userPs.setObject(7, user.getId());
                int updatedRows = userPs.executeUpdate();
                if (updatedRows == 0) {
                    throw new IllegalStateException("Can't find id");
                }

                authDelPs.setObject(1, user.getId());
                authDelPs.executeUpdate();

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authPs.setObject(1, user.getId());
                    authPs.setString(2, authority.getAuthority().name());
                    authPs.addBatch();
                    authPs.clearParameters();
                }
                authPs.executeBatch();

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInUserdata(UserEntity user) {
        try (Connection connection = udDs.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE \"user\" SET username = ?, currency = ?" +
                    "firstname = ?, surname = ?, photo = ? WHERE id = ?")) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().name());
                ps.setString(3, user.getFirstname());
                ps.setString(4, user.getSurname());
                ps.setBytes(5, user.getPhoto());
                ps.setObject(6, user.getId());

                int updatedRows = ps.executeUpdate();
                if (updatedRows == 0) {
                    throw new IllegalArgumentException("Can't find id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteInAuthById(UUID id) {
        try (Connection connection = authDs.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement userPs = connection.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
                 PreparedStatement authPs = connection.prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?")) {

                authPs.setObject(1, id);
                userPs.setObject(1, id);
                authPs.executeUpdate();
                userPs.executeUpdate();

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteInUserdataById(UUID id) {
        try (Connection connection = udDs.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement userPs = connection.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
                 PreparedStatement friendsPs = connection.prepareStatement("DELETE FROM friendship WHERE user_id = ?");
                 PreparedStatement invitesPs = connection.prepareStatement("DELETE FROM friendship WHERE friend_id = ?")) {

                userPs.setObject(1, id);
                friendsPs.setObject(1, id);
                invitesPs.setObject(1, id);
                friendsPs.executeUpdate();
                invitesPs.executeUpdate();
                userPs.executeUpdate();

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
