package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.category.CategoryApi;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class CategoryExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace CATEGORY_NAMESPACE = ExtensionContext.Namespace
            .create(CategoryExtension.class);
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final CategoryApi categoryApi = RETROFIT.create(CategoryApi.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateCategory> category = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class
        );

        if (category.isPresent()) {
            GenerateCategory categoryData = category.get();
            CategoryJson categoryJson = new CategoryJson(
                    null,
                    categoryData.category(),
                    categoryData.username()
            );

            categoryApi.getCategories(categoryData.username())
                            .execute()
                            .body().stream()
                    .filter(c -> Objects.equals(c.category(), categoryData.category()))
                    .findFirst()
                    .or(() -> {
                        CategoryJson created = null;
                        try {
                            created = categoryApi.addCategory(categoryJson)
                                    .execute()
                                    .body();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return Optional.of(created);
                    })
                    .ifPresent(created -> {
                        extensionContext.getStore(CATEGORY_NAMESPACE)
                                .put(extensionContext.getUniqueId(), created);
                    });
        }
    }
}
