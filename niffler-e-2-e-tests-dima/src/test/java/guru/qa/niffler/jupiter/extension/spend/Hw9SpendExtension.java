package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.jupiter.annotation.Hw9GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;
import java.util.Optional;

public abstract class Hw9SpendExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(Hw9SpendExtension.class);

    abstract SpendJson create(SpendJson spend);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<Hw9GenerateSpend> spend = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Hw9GenerateSpend.class
        );

        if (spend.isPresent()) {
            Hw9GenerateSpend spendData = spend.get();
            SpendJson spendJson = new SpendJson(
                    null,
                    new Date(),
                    spendData.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    spendData.username()
                    );

            SpendJson created = create(spendJson);

            extensionContext.getStore(NAMESPACE)
                    .put(extensionContext.getUniqueId(), created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType()
                .isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
