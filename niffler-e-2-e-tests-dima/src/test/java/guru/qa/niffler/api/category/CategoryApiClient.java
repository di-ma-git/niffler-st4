package guru.qa.niffler.api.category;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class CategoryApiClient extends RestClient {

    private final CategoryApi categoryApi;

    public CategoryApiClient(@Nonnull String baseUri) {
        super(Config.getInstance().frontUrl());
        this.categoryApi = retrofit.create(CategoryApi.class);
    }


    @Step("Get all {username}'s categories")
    public List<CategoryJson> getCategories(String username) throws IOException {
        return categoryApi.getCategories(username).execute().body();
    }

    @Step("Create new category {category}")
    public CategoryJson addCategory(CategoryJson category) throws IOException {
        return categoryApi.addCategory(category).execute().body();
    }

}
