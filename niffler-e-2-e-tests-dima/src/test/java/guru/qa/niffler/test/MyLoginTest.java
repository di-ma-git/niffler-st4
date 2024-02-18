package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.repository.user.UserRepository;
import guru.qa.niffler.db.model.enums.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.enums.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.user.UserRepositorySJdbc;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(UserRepositoryExtension.class)
public class MyLoginTest extends BaseWebTest {

//    private UserRepository userRepository;
    private UserRepository userRepository = new UserRepositorySJdbc();
    private UserAuthEntity userAuth;
    private UserEntity user;
    private final LoginPage loginPage = new LoginPage();


    @BeforeEach
    void createUser() {
        userAuth = new UserAuthEntity();
        userAuth.setUsername("valentin20");
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
        user.setUsername("valentin20");
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

    @DbUser(username = "vallentin21", password = "12345")
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

    @Test
    void updateUserInUserdata(){
        user.setCurrency(CurrencyValues.KZT);
        userRepository.updateInUserdata(user);

        Optional<UserEntity> result = userRepository.findInUserdataById(user.getId());

        assertThat(result).equals(CurrencyValues.KZT);
    }

    @Test
    void updateUserInAuth(){
        userAuth.setUsername("semen123");
        userRepository.updateInAuth(userAuth);

        Optional<UserAuthEntity> result = userRepository.findInAuthById(userAuth.getId());

        assertThat(result.get().getUsername()).equals(userAuth.getUsername());
    }
}
