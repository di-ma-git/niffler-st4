package guru.qa.niffler.api.userdata;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class FriendsApiClient extends RestClient {

    private final FriendsApi friendsApi;

    public FriendsApiClient() {
        super(Config.getInstance().frontUrl());
        this.friendsApi = retrofit.create(FriendsApi.class);
    }

    @Step("Get all friends for {username}")
    public List<UserJson> friends(String username, boolean includePending) throws IOException {
        return friendsApi.friends(username, includePending).execute().body();
    }

    @Step("Get all invitations for {username}")
    public List<UserJson> invitations(String username) throws IOException {
        return friendsApi.invitations(username).execute().body();
    }

    @Step("Accept {invitation} invitation for {username}")
    public List<UserJson> acceptInvitation(String username, FriendJson invitation) throws IOException {
        return friendsApi.acceptInvitation(username, invitation).execute().body();
    }

    @Step("Decline {invitation} invitation for {username}")
    public List<UserJson> declineInvitation(String username, FriendJson invitation) throws IOException {
        return friendsApi.declineInvitation(username, invitation).execute().body();
    }

    @Step("Add {invitation} invitation friend for {username}")
    public UserJson addFriend(String username, FriendJson invitation) throws IOException {
        return friendsApi.addFriend(username, invitation).execute().body();
    }

    @Step("Delete {friendUsername} from {username}'s friends")
    public List<UserJson> removeFriend(String username, String friendUsername) throws IOException {
        return friendsApi.removeFriend(username, friendUsername).execute().body();
    }
}
