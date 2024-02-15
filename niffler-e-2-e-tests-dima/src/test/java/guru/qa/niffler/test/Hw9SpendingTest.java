package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.Hw9GenerateSpend;
import guru.qa.niffler.jupiter.extension.spend.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

public class Hw9SpendingTest extends BaseWebTest {

    private final MainPage mainPage = new MainPage();
    private final LoginPage loginPage = new LoginPage();

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void init() {
        Selenide.open("http://127.0.0.1:3000/main");

        loginPage.redirectToLogin()
                .checkTitle()
                .login("vova", "12345");

    }

    @AfterEach
    void closeBrowser() {

    }

    @Hw9GenerateSpend(
            username = "vova",
            description = "QA.GURU Advanced 4",
            amount = 14000.00,
            currency = CurrencyValues.RUB,
            category = "education"
    )
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpendingRest(SpendJson spend) {
        mainPage.findSpendingByDescription(spend.description())
                .pressDeleteButton()
                .checkRows(0);
    }

}
