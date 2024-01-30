package guru.qa.niffler.test.friends;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTestBeforeEach {

    private final LoginPage loginPage = new LoginPage();
    private final FriendsPage friendsPage = new FriendsPage();

    @BeforeEach
    void init(@User(WITH_FRIENDS) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/main");
        loginPage.redirectToLogin()
                .checkTitle()
                .login(user.username(), user.testData().password());

    }

    @Test
    void userShouldHaveFriendFromBeforeEach() {
        friendsPage.checkFriendIsPresent();
    }

}
