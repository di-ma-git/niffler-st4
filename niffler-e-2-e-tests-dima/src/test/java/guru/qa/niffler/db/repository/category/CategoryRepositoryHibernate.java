package guru.qa.niffler.db.repository.category;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.db.JdbcUrl.SPEND;

public class CategoryRepositoryHibernate extends JpaService implements CategoryRepository {

    public CategoryRepositoryHibernate() {
        super(SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager());
    }
    @Override
    public CategoryEntity create(CategoryEntity category) {
        persist(SPEND, category);
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
