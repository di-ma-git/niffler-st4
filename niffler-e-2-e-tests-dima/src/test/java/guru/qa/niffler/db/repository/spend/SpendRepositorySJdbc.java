package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.SpendEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SpendRepositorySJdbc implements SpendRepository {

    private final JdbcTemplate template = new JdbcTemplate(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.SPEND));


    @Override
    public SpendEntity create(SpendEntity spend) {
        KeyHolder kh = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO \"spend\" " +
                    "(username, spend_date, currency, amount, description, category_id)" +
                    "VALUES (?,?,?,?,?,?)", RETURN_GENERATED_KEYS);
            ps.setString(1, spend.getUsername());
            ps.setDate(2, (Date) spend.getSpendDate());
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            return ps;
        }, kh);

        spend.setId((UUID) kh.getKeys().get("id"));
        return spend;
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
