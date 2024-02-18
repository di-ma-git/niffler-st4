package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.SpendEntity;
import jakarta.persistence.EntityManager;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.db.JdbcUrl.*;

public class SpendRepositoryHibernate extends JpaService implements SpendRepository{

    public SpendRepositoryHibernate() {
        super(SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager());
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        persist(SPEND, spend);
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
