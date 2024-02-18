package guru.qa.niffler.db.repository.category;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

    CategoryEntity create(CategoryEntity category);

    Optional<CategoryEntity> findById(UUID id);

    void update(CategoryEntity category);

    void delete (UUID id);
}
