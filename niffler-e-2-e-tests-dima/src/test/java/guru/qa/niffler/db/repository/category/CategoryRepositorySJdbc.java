package guru.qa.niffler.db.repository.category;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.CategoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;

public class CategoryRepositorySJdbc implements CategoryRepository {

    private final JdbcTemplate template = new JdbcTemplate(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.SPEND));

    @Override
    public CategoryEntity create(CategoryEntity category) {
        KeyHolder kh = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO category (category, username)" +
                    "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            return ps;
        }, kh);
        category.setId((UUID) kh.getKeys().get("id"));
        return category;
    }

    @Override
    public Optional<CategoryEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public void update(CategoryEntity category) {

    }

    @Override
    public void delete(UUID id) {

    }
}
