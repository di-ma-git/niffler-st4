package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final String TITLE_TEXT = "Welcome to Niffler. The coin keeper";
    private final SelenideElement
            title = $(".form__header"),
            nameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            redirect = $("a[href*='redirect']");


    public LoginPage redirectToLogin() {
        redirect.click();
        return this;
    }

    public LoginPage checkTitle() {
        title.shouldHave(text(TITLE_TEXT));
        return this;
    }

    public LoginPage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public LoginPage pressSubmit() {
        submitButton.click();
        return this;
    }
}
