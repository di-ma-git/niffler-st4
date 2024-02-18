package guru.qa.niffler.db.repository.user;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.db.JdbcUrl.AUTH;
import static guru.qa.niffler.db.JdbcUrl.USERDATA;

public class UserRepositoryHibernate extends JpaService implements UserRepository {

    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserRepositoryHibernate() {

        super(Map.of(
                AUTH, EmfProvider.INSTANCE.emf(AUTH).createEntityManager(),
                USERDATA, EmfProvider.INSTANCE.emf(USERDATA).createEntityManager()
        ));
    }

    @Override

    public UserAuthEntity createInAuth(UserAuthEntity user) {
        String rawPassword = user.getPassword();
        user.setPassword(pe.encode(user.getPassword()));
        persist(AUTH, user);
        user.setPassword(rawPassword);
        return user;
    }

    @Override
    public UserEntity createInUserdata(UserEntity user) {
        persist(USERDATA, user);
        return user;
    }

    @Override
    public Optional<UserAuthEntity> findInAuthById(UUID id) {
        return Optional.ofNullable(entityManager(AUTH).find(UserAuthEntity.class, id));
    }

    @Override
    public Optional<UserEntity> findInUserdataById(UUID id) {
        return Optional.ofNullable(entityManager(USERDATA).find(UserEntity.class, id));

    }

    @Override
    public void updateInAuth(UserAuthEntity user) {
        merge(AUTH, user);
    }

    @Override
    public void updateInUserdata(UserEntity user) {
        merge(USERDATA, user);
    }

    @Override
    public void deleteInAuthById(UUID id) {
        UserAuthEntity toBeDeleted = findInAuthById(id).get();
        remove(AUTH, toBeDeleted);
    }

    @Override
    public void deleteInUserdataById(UUID id) {
        UserEntity toBeDeleted = findInUserdataById(id).get();
        remove(USERDATA, toBeDeleted);
    }
}
