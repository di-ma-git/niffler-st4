package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDbTest extends BaseWebTest {

    @BeforeEach
    void init() {

    }

    @DbUser(username = "valentin", password = "12345")
    @Test
    void statisticShouldBeVisibleAfterLogin() {

    }
}
