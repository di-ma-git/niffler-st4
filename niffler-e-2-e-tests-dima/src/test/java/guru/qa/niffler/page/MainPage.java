package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final SelenideElement deleteButton = $(byText("Delete selected")),
            spendingTableBody = $(".spendings-table tbody");

    public MainPage findSpendingByDescription(String description) {
        spendingTableBody.$$("tr")
                .find(text(description))
                .$$("td")
                .first().scrollTo()
//                .scrollIntoView(true)
                .click();

        return this;
    }

    public MainPage pressDeleteButton() {
        deleteButton.click();
        return this;
    }

    public MainPage checkRows(int size) {
        spendingTableBody.$$("tr")
                .shouldHave(CollectionCondition.size(size));
        return this;
    }
}
