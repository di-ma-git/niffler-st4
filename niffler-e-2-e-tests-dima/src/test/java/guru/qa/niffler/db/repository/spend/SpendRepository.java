package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

    SpendEntity create(SpendEntity spend);

    SpendEntity createSpending(SpendEntity spend);

    Optional<SpendEntity> findById(UUID id);

    void update(SpendEntity spend);

    void delete (UUID id);
}
