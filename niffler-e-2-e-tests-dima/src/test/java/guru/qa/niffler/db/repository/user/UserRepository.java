package guru.qa.niffler.db.repository.user;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createInAuth(UserAuthEntity user);
    UserEntity createInUserdata(UserEntity user);
    Optional<UserAuthEntity> findInAuthById(UUID id);
    Optional<UserEntity> findInUserdataById(UUID id);
    void updateInAuth(UserAuthEntity user);
    void updateInUserdata(UserEntity user);
    void deleteInAuthById(UUID id);
    void deleteInUserdataById(UUID id);



}
