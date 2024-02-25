package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {

    private final String TITLE_TEXT = "Welcome to Niffler. The coin keeper";

    private final SelenideElement loginBtn = $("a[href*='redirect']");
    private final SelenideElement registerBtn = $("a[href*='auth']");
    private final SelenideElement title = $(".form__header");

    @Step("Check title \"Welcome to Niffler. The coin keeper\"")
    public WelcomePage checkTitle() {
        title.shouldHave(text(TITLE_TEXT));
        return this;
    }

    @Step("Redirect to LogIn")
    public LoginPage redirectToLoginPage() {
        loginBtn.click();
        return new LoginPage();
    }

    @Step("Redirect to Registration")
    public RegisterPage redirectToRegisterPage() {
        registerBtn.click();
        return new RegisterPage();
    }
}
