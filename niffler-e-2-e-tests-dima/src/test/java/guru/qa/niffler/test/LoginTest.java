package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.UserRepositoryExtension;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Optional;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(UserRepositoryExtension.class)
public class LoginTest extends BaseWebTest {

    private UserRepository userRepository;
    private UserAuthEntity userAuth;
    private UserEntity user;
    private final LoginPage loginPage = new LoginPage();


    @BeforeEach
    void createUser() {
        userAuth = new UserAuthEntity();
        userAuth.setUsername("valentin13");
        userAuth.setPassword("12345");
        userAuth.setEnabled(true);
        userAuth.setAccountNonExpired(true);
        userAuth.setAccountNonLocked(true);
        userAuth.setCredentialsNonExpired(true);
        userAuth.setAuthorities(Arrays.stream(Authority.values())
                .map(e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(e);
                    return ae;
                }).toList());

        user = new UserEntity();
        user.setUsername("valentin13");
        user.setCurrency(CurrencyValues.RUB);

        userRepository.createInAuth(userAuth);
        userRepository.createInUserdata(user);
    }

    @AfterEach
    void removeUser() {
        userRepository.deleteInAuthById(userAuth.getId());
        userRepository.deleteInUserdataById(user.getId());
    }

    @Test
    void statisticShouldBeVisibleAfterLogin() {
        Optional<UserAuthEntity> inAuthById = userRepository.findInAuthById(userAuth.getId());
        Optional<UserEntity> inUserdataById = userRepository.findInUserdataById(user.getId());


        Selenide.open("http://127.0.0.1:3000");
        loginPage.redirectToLogin()
                .checkTitle();
        loginPage.login(userAuth.getUsername(), userAuth.getPassword());

        $(".main-content__section-stats").shouldBe(visible);

    }

    @DbUser(username = "vallentin17", password = "12345")
    @Test
    void statisticShouldBeVisibleAfterLoginDbUser(UserAuthEntity userAuth) {
        Selenide.open("http://127.0.0.1:3000");
        loginPage.redirectToLogin()
                .checkTitle();
        loginPage.login(userAuth.getUsername(), userAuth.getPassword());

        $(".main-content__section-stats").shouldBe(visible);

    }

    @DbUser
    @Test
    void statisticShouldBeVisibleAfterLoginFakerDbUser(UserAuthEntity userAuth) {
        Selenide.open("http://127.0.0.1:3000");
        loginPage.redirectToLogin()
                .checkTitle();
        loginPage.login(userAuth.getUsername(), userAuth.getPassword());

        $(".main-content__section-stats").shouldBe(visible);

    }
}
