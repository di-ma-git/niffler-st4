package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.repository.user.UserRepository;
import guru.qa.niffler.db.repository.user.UserRepositoryJdbc;
import guru.qa.niffler.db.repository.user.UserRepositorySJdbc;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class UserRepositoryExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) throws Exception {
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(UserRepository.class)) {
                field.setAccessible(true);

                UserRepository userRepository;
                String repository = System.getProperty("repository");
                switch (repository) {
                    case "jdbc" -> userRepository = new UserRepositoryJdbc();
                    case "sjdbc" -> userRepository = new UserRepositorySJdbc();
                    default -> throw new RuntimeException("Choose correct repository argument: 'jdbc' or 'sjdbc'");
                }
                field.set(o, repository);
            }
        }
    }
}
