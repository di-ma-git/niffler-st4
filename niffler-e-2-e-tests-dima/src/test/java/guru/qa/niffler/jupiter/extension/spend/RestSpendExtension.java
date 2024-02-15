package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestSpendExtension extends Hw9SpendExtension {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = RETROFIT.create(SpendApi.class);
    private final CategoryApi categoryApi = RETROFIT.create(CategoryApi.class);


    @Override
    @SneakyThrows
    SpendJson create(SpendJson spend) {
        CategoryJson category = new CategoryJson(
                null,
                spend.category(),
                spend.username()
        );

        categoryApi.addCategory(category).execute().body();

        return spendApi.addSpend(spend).execute().body();
    }
}
