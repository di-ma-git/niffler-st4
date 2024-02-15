package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.User.UserType.COMMON;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(UsersQueueExtension.class);

    private static final Map<User.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> commonQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> invitationSentQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> invitationReceivedQueue = new ConcurrentLinkedQueue<>();

        friendsQueue.add(user("dima", "12345", WITH_FRIENDS, "duck"));
        friendsQueue.add(user("duck", "12345", WITH_FRIENDS, "dima"));
        commonQueue.add(user("bee", "12345", COMMON, null));
        commonQueue.add(user("barsik", "12345", COMMON, null));
        invitationSentQueue.add(user("vitalik", "12345", INVITATION_SENT, "semen"));
        invitationSentQueue.add(user("tanya", "12345", INVITATION_SENT, "katya"));
        invitationReceivedQueue.add(user("semen", "12345", INVITATION_RECEIVED, "vitalik"));
        invitationReceivedQueue.add(user("katya", "12345", INVITATION_RECEIVED, "tanya"));

        users.put(WITH_FRIENDS, friendsQueue);
        users.put(COMMON, commonQueue);
        users.put(INVITATION_SENT, invitationSentQueue);
        users.put(INVITATION_RECEIVED, invitationReceivedQueue);
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        List<Parameter> parameters = new ArrayList<>();

        Arrays.stream(extensionContext.getRequiredTestMethod().getParameters())
                .filter(p -> p.isAnnotationPresent(User.class))
                .filter(p -> p.getType().isAssignableFrom(UserJson.class))
                .forEach(parameters::add);

        Arrays.stream(extensionContext.getRequiredTestClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(BeforeEach.class))
                .flatMap(m -> Arrays.stream(m.getParameters()))
                .filter(p -> p.getType().isAssignableFrom(UserJson.class) && p.getAnnotation(User.class) != null)
                .forEach(parameters::add);

        Map<User.UserType, UserJson> testCandidates = new HashMap<>();

        for (Parameter parameter : parameters) {
            User annotation = parameter.getAnnotation(User.class);
            if (testCandidates.get(annotation.value()) != null) {
                continue;
            }
            User.UserType value = annotation.value();
            Queue<UserJson> queue = users.get(annotation.value());
            UserJson testCandidate = null;
            while (testCandidate == null) {
                testCandidate = queue.poll();
            }
            testCandidates.put(annotation.value(), testCandidate);
        }
        extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), testCandidates);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Map<User.UserType, UserJson> usersFromTest = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);
        for (User.UserType userType : usersFromTest.keySet()) {
            users.get(userType).add(usersFromTest.get(userType));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType().isAssignableFrom(UserJson.class) &&
                parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserJson) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(parameterContext.findAnnotation(User.class).get().value());
    }

    private static UserJson user(String username, String password, User.UserType userType, String friend) {
        return new UserJson(
                null,
                username,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                new TestData(
                        password,
                        userType,
                        friend
                )
        );

    }
}
