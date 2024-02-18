package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
<<<<<<< HEAD
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
=======
>>>>>>> upstream/master
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

@ExtendWith(UserRepositoryExtension.class)
public class LoginTest extends BaseWebTest {

  private UserRepository userRepository;

  private UserAuthEntity userAuth;
  private UserEntity user;


  @BeforeEach
  void createUser() {
    userAuth = new UserAuthEntity();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    userAuth.setUsername("valentin_5");
=======
    userAuth.setUsername("valentin_6");
>>>>>>> 766df2b7dbc50208234691faa37344cc2bad0403
=======
    userAuth.setUsername("valentin_7");
>>>>>>> upstream/master
=======
    userAuth.setUsername("valentin_5");
>>>>>>> ed17acb6e376860cb98de8547d2c2d8c726f2da8
    userAuth.setPassword("12345");
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);

    AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    userAuth.addAuthorities(authorities);

    user = new UserEntity();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    user.setUsername("valentin_5");
=======
    user.setUsername("valentin_6");
>>>>>>> 766df2b7dbc50208234691faa37344cc2bad0403
=======
    user.setUsername("valentin_7");
>>>>>>> upstream/master
=======
    user.setUsername("valentin_5");
>>>>>>> ed17acb6e376860cb98de8547d2c2d8c726f2da8
    user.setCurrency(CurrencyValues.RUB);
    userRepository.createInAuth(userAuth);
    userRepository.createInUserdata(user);
  }

  @AfterEach
  void removeUser() {
    userRepository.deleteInAuthById(userAuth.getId());
    userRepository.deleteInUserdataById(user.getId());
  }

  @DbUser()
  @Test
  void statisticShouldBeVisibleAfterLogin() {
    Selenide.open(WelcomePage.URL, WelcomePage.class)
        .doLogin()
        .fillLoginPage(userAuth.getUsername(), userAuth.getPassword())
        .submit();

    new MainPage()
        .waitForPageLoaded();
  }
}
