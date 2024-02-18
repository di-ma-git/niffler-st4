package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;
import java.util.Optional;

public class SpendExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace SPEND_NAMESPACE
            = ExtensionContext.Namespace.create(SpendExtension.class);

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = RETROFIT.create(SpendApi.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateSpend> spend = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateSpend.class
        );

        Optional<GenerateCategory> category = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class
        );

        if (spend.isPresent() && category.isPresent()) {
            GenerateSpend spendData = spend.get();

            CategoryJson categoryJson = extensionContext.getStore(CategoryExtension.CATEGORY_NAMESPACE)
                    .get(extensionContext.getUniqueId(), CategoryJson.class);
            SpendJson spendJson = new SpendJson(
                    null,
                    new Date(),
                    categoryJson.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    categoryJson.username()
                    );

            SpendJson created = spendApi.addSpend(spendJson).execute().body();
            extensionContext.getStore(SPEND_NAMESPACE)
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
                .getStore(SpendExtension.SPEND_NAMESPACE)
                .get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
