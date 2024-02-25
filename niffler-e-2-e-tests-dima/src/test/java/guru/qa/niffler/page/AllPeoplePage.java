package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.ui.Select;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage extends BasePage<AllPeoplePage> {

    private final SelenideElement goToAllPeoplePage = $("a[href='/people'");

    public AllPeoplePage checkPendingInvitation(String friend) {
        goToAllPeoplePage.click();
        $(".people-content tbody")
                .$$("tr")
                .findBy(text(friend))
                .$$("td").get(3)
                .shouldHave(exactText("Pending invitation"));
        return this;
    }

}
