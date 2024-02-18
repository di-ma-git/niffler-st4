package guru.qa.niffler.test.friends;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTest {

    private final LoginPage loginPage = new LoginPage();
    private final FriendsPage friendsPage = new FriendsPage();
    private final AllPeoplePage allPeoplePage = new AllPeoplePage();

    @BeforeEach
    void init() {
//        Selenide.open("http://127.0.0.1:3000/main");
        Selenide.open("http://127.0.0.1:3000");
        loginPage.redirectToLogin()
                .checkTitle();
    }

    @Test
    void userShouldHaveFriend(@User(WITH_FRIENDS) UserJson user) {
        loginPage.login(user.username(), user.testData().password());
        friendsPage.checkFriendIsPresent();
    }

    @Test
    void userShouldHavePendingInvitationWhenHeSentFriendsRequest(@User(INVITATION_SENT) UserJson user) {
        loginPage.login(user.username(), user.testData().password());
        allPeoplePage.checkPendingInvitation(user.testData().friendUsername());
    }

    @Test
    void userShouldHaveSubmitOrDeclineInvitationWhenReceivedFriendsRequest(@User(INVITATION_RECEIVED) UserJson user) {
        loginPage.login(user.username(), user.testData().password());
        friendsPage.checkReceivedInvitation(user.testData().friendUsername());
    }

}
