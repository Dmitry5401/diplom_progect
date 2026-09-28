package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ReservationPage {

    private static final String CHECKIN = "2026-10-01";
    private static final String CHECKOUT = "2026-10-02";

    private final SelenideElement
        roomTitle = $("h1"),
        bookingCard = $(".booking-card"),
        roomDetails = $(".col-lg-8"),
        page = $("body");

    public void openReservation(int roomId) {
        open("/reservation/" + roomId + "?checkin=" + CHECKIN + "&checkout=" + CHECKOUT);
    }

    public void checkRoomDisplayed(String type, int price, String description) {
        roomTitle.shouldHave(text(type + " Room"));
        bookingCard.shouldBe(visible).shouldHave(text("£" + price));
        roomDetails.shouldHave(text(description));
    }

    public void checkFeatures(String... features) {
        for (String feature : features) {
            roomDetails.shouldHave(text(feature));
        }
    }

    public void checkRoomNotDisplayed(String description) {
        page.shouldNotHave(text(description));
    }
}
