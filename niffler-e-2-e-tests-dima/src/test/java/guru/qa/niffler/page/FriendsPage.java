package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage<FriendsPage> {

    private final SelenideElement goToFriendsPage = $("a[href='/friends']");

    public FriendsPage checkFriendIsPresent() {
        goToFriendsPage.click();
        $(".abstract-table tbody")
                .$$("tr")
                .first()
                .shouldHave(text("You are friends"));
        return this;
    }

    public FriendsPage checkReceivedInvitation(String friend) {
        goToFriendsPage.click();
        $(".people-content tbody")
                .$$("tr")
                .findBy(text(friend))
                .$("[data-tooltip-id='submit-invitation']")
                .shouldBe(visible);
        return this;
    }
}
