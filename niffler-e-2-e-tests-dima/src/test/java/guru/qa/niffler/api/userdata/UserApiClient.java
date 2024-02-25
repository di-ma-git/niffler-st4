package guru.qa.niffler.api.userdata;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class UserApiClient extends RestClient {

    private final UserApi userApi;

    public UserApiClient(@Nonnull String baseUri) {
        super(Config.getInstance().frontUrl());
        this.userApi = retrofit.create(UserApi.class);
    }

    @Step("Update userdata for {username}")
    public UserJson updateUserInfo(UserJson user) throws IOException {
        return userApi.updateUserInfo(user).execute().body();
    }

    @Step("Get userdata for current user")
    public UserJson currentUser(String username) throws IOException {
        return userApi.currentUser(username).execute().body();
    }

    @Step("Get all users")
    public List<UserJson> allUsers(String username) throws IOException {
        return userApi.allUsers(username).execute().body();
    }

}
