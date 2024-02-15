package guru.qa.niffler.db.jpa;

import guru.qa.niffler.db.JdbcUrl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class JpaService {

    private final Map<JdbcUrl, EntityManager> emStore;

    public JpaService(JdbcUrl jdbcUrl, EntityManager em) {
        this.emStore = new HashMap<>();
        this.emStore.put(jdbcUrl, em);
    }

    public JpaService(Map<JdbcUrl, EntityManager> ems) {
        this.emStore = ems;
    }

    protected EntityManager entityManager(JdbcUrl jdbcUrl) {
        return emStore.get(jdbcUrl);
    }

    protected <T> void persist(JdbcUrl jdbcUrl, T entity) {
        tx(jdbcUrl, em -> em.persist(entity));
    }

    protected <T> void remove(JdbcUrl jdbcUrl, T entity) {
        tx(jdbcUrl, em -> em.remove(entity));
    }

    protected <T> T merge(JdbcUrl jdbcUrl, T entity) {
        return txWithResult(jdbcUrl, em -> em.merge(entity));
    }

    protected <T> T txWithResult(JdbcUrl jdbcUrl, Function<EntityManager, T> function) {
        EntityTransaction transaction = emStore.get(jdbcUrl).getTransaction();
        transaction.begin();
        try {
            T result = function.apply(emStore.get(jdbcUrl));
            transaction.commit();
            return result;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    protected void tx(JdbcUrl jdbcUrl, Consumer<EntityManager> consumer) {
        EntityTransaction transaction = emStore.get(jdbcUrl).getTransaction();
        transaction.begin();
        try {
            consumer.accept(emStore.get(jdbcUrl));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
