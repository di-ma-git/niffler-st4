package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByText;
import guru.qa.niffler.model.CurrencyValues;

public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement nameInput = $("input[name='firstname']");
    private final SelenideElement surnameInput = $("input[name='surname']");
    private final SelenideElement submitBtn = $("button[type='submit']");

    private final SelenideElement categoryNameInput = $("input[name='category']");
    private final SelenideElement createCategoryBtn = $(".add-category__input-container button");

    private final SelenideElement avatarEditBtn = $(".profile__avatar-edit");
    private final SelenideElement uploadFileBtn = $(".edit-avatar__input[type='file']");

    private final ElementsCollection categories = $$(".categories__list .categories__item");


    public ProfilePage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage setSurname(String surname) {
        surnameInput.setValue(surname);
        return this;
    }
    public ProfilePage chooseCurrency(CurrencyValues currency) {
        $(". css-1xc3v61-indicatorContainer").selectOptionByValue(currency.name());
        return this;
    }

    public ProfilePage submitName() {
        submitBtn.click();
        return this;
    }

    public ProfilePage setCategoryName(String name) {
        categoryNameInput.setValue(name);
        return this;
    }

    public ProfilePage addCategoryBtnClick() {
        createCategoryBtn.click();
        return this;
    }

    public ProfilePage categoriesListContains(String category) {
        categories.texts().contains(category);
        return this;
    }


}
