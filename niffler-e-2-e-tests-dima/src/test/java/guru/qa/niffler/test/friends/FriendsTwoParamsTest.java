package guru.qa.niffler.test.friends;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.UsersQueueExtension;
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

@ExtendWith(UsersQueueExtension.class)
public class FriendsTwoParamsTest {

    private final LoginPage loginPage = new LoginPage();
    private final FriendsPage friendsPage = new FriendsPage();
    private final AllPeoplePage allPeoplePage = new AllPeoplePage();

    @BeforeEach
    void init() {
        Selenide.open("http://127.0.0.1:3000/main");
        loginPage.redirectToLogin()
                .checkTitle();
    }

    @Test
    void friendsTableShouldNotBeEmpty(@User(INVITATION_SENT) UserJson userInvitationSend,
                                       @User(INVITATION_RECEIVED) UserJson userInvitationReceived) {
        loginPage.login(userInvitationSend.username(), userInvitationSend.testData().password());
        allPeoplePage.checkPendingInvitation(userInvitationReceived.username());
    }
}
