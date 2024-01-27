package guru.qa.niffler.page.component;

import com.codeborne.selenide.CollectionCondition;

import static com.codeborne.selenide.Selenide.$;

public class SpendingTableComponent {

    public void checkRows(int size) {
        $(".spendings-table tbody")
                .$$("tr")
                .shouldHave(CollectionCondition.size(size));
    }
}
