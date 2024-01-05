package guru.qa.niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTest {

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
                .setName("duck")
                .setPassword("12345")
                .pressSubmit();
    }

    @GenerateCategory(
            username = "duck",
            category = "Обучение"
    )
    @GenerateSpend(
            username = "duck",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            currency = CurrencyValues.RUB
    )


    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend){
        mainPage.findSpendingByDescription(spend.description())
                .pressDeleteButton()
                .checkRows(0);
    }
}
