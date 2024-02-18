package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.SpendEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private static final DataSource ds = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.SPEND);


    @Override
    public SpendEntity create(SpendEntity spend) {
        try (Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO spend (" +
                    "username, spend_date, currency, amount, description, category_id)" +
                    "VALUES (?, ?, ?, ?, ?, ?)")) {

                ps.setString(1, spend.getUsername());
                ps.setDate(2, (Date) spend.getSpendDate());
                ps.setString(3, spend.getCurrency().name());
                ps.setDouble(4, spend.getAmount());
                ps.setString(5, spend.getDescription());
                ps.setObject(6, spend.getCategory().getId());

                ps.executeUpdate();

                ResultSet resultSet = ps.getResultSet();
                spend.setId(resultSet.getObject("id", UUID.class));

                return spend;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity createSpending(SpendEntity spend) {
        return null;
    }


    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public void update(SpendEntity spend) {
    }

    @Override
    public void delete(UUID id) {
    }
}
