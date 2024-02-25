package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

    private final String SUCCESS_REGISTRATION_TEXT = "Congratulations! You've registered!";

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement confirmPasswordInput = $("#passwordSubmit");
    private final SelenideElement submitBtn = $("button.form__submit");
    private final SelenideElement signInBtn = $("a[href*='redirect']");
    private final SelenideElement title = $(".form__paragraph");


    public LoginPage successfulRegistration(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        confirmPasswordInput.setValue(password);
        submitBtn.click();
        title.shouldHave(text(SUCCESS_REGISTRATION_TEXT));
        signInBtn.click();

        return new LoginPage();
    }

    public RegisterPage failedRegistration(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        confirmPasswordInput.setValue(password);
        submitBtn.click();

        return this;
    }

}
