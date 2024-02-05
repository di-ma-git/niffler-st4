package guru.qa.niffler.jupiter;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UserDbExtension implements ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(UserDbExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {


        
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return null;
    }
}
