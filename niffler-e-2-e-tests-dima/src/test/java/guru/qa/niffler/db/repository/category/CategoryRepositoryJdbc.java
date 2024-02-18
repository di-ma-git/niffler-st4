package guru.qa.niffler.db.repository.category;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.CategoryEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class CategoryRepositoryJdbc implements CategoryRepository {

    private static final DataSource ds = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.SPEND);


    @Override
    public CategoryEntity create(CategoryEntity category) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO category (category, username)" +
                     "VALUES (?, ?)")) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());

            ps.executeUpdate();

            ResultSet resultSet = ps.getResultSet();
            category.setId(resultSet.getObject("id", UUID.class));

            return category;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
