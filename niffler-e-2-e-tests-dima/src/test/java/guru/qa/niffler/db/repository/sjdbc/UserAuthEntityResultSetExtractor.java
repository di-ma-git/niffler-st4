package guru.qa.niffler.db.repository.sjdbc;

import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserAuthEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserAuthEntityResultSetExtractor implements ResultSetExtractor<UserAuthEntity> {

    public static final UserAuthEntityResultSetExtractor INSTANCE = new UserAuthEntityResultSetExtractor();

    @Override
    public UserAuthEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        UserAuthEntity user = new UserAuthEntity();

        boolean isProcessed = false;
        while (rs.next()) {
            if (!isProcessed) {
                user = new UserAuthEntity();
                user.setId(rs.getObject(1, UUID.class));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEnabled(rs.getBoolean(4));
                user.setAccountNonExpired(rs.getBoolean(5));
                user.setAccountNonLocked(rs.getBoolean(6));
                user.setCredentialsNonExpired(rs.getBoolean(7));
                isProcessed = true;
            }

            if (rs.getString(10) != null) {
                AuthorityEntity authority = new AuthorityEntity();
                authority.setId(rs.getObject(8, UUID.class));
                authority.setAuthority(Authority.valueOf(rs.getString(10)));
                user.getAuthorities().add(authority);
            }
        }
        return user;

    }


}
