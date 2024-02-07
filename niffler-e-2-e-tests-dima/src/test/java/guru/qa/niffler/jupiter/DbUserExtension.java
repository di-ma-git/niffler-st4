package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DbUserExtension implements ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(DbUserExtension.class);

    private final UserRepository userRepository = new UserRepositoryJdbc();

    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<DbUser> dbUserAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), DbUser.class);

        if (dbUserAnnotation.isPresent()) {
            DbUser dbUser = dbUserAnnotation.get();

            String username = dbUser.username().isEmpty()
                    ? faker.name().username()
                    : dbUser.username();
            String password = dbUser.password().isEmpty()
                    ? faker.internet().password()
                    : dbUser.password();

            UserAuthEntity userAuth = new UserAuthEntity();
            userAuth.setUsername(username);
            userAuth.setPassword(password);
            userAuth.setEnabled(true);
            userAuth.setAccountNonExpired(true);
            userAuth.setAccountNonLocked(true);
            userAuth.setCredentialsNonExpired(true);
            userAuth.setAuthorities(Arrays.stream(Authority.values())
                    .map(a -> {
                       AuthorityEntity authority = new AuthorityEntity();
                       authority.setAuthority(a);
                       return authority;
                    })
                    .toList());

            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setCurrency(CurrencyValues.RUB);

            Map<String, Object> userData = new HashMap<>();

            userRepository.createInAuth(userAuth);
            userRepository.createInUserdata(user);

            userData.put("userAuth", userAuth);
            userData.put("user", user);

            extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), userData);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Optional<DbUser> dbUserAnnotation = AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), DbUser.class);

        if (dbUserAnnotation.isPresent()) {
            Map<String, Object> userData = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
            userRepository.deleteInAuthById(((UserAuthEntity) userData.get("userAuth")).getId());
            userRepository.deleteInUserdataById(((UserEntity) userData.get("user")).getId());
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), DbUser.class).isPresent() &&
                parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class).get("userAuth");
    }
}
