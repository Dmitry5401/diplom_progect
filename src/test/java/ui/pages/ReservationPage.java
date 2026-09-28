package ui.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ReservationPage {

    private static final String CHECKIN = "2026-10-01";
    private static final String CHECKOUT = "2026-10-02";

    @Step("Открыть страницу бронирования комнаты id={roomId}")
    public ReservationPage openReservation(int roomId) {
        open("/reservation/" + roomId + "?checkin=" + CHECKIN + "&checkout=" + CHECKOUT);
        return this;
    }

    @Step("Проверить, что на странице отображается комната: тип '{type}', цена £{price}")
    public ReservationPage checkRoomDisplayed(String type, int price, String description) {
        $("h1").shouldHave(exactText(type + " Room"));
        $(".booking-card").shouldBe(visible).shouldHave(text("£" + price));
        $(".col-lg-8").shouldHave(text(description));
        return this;
    }

    @Step("Проверить наличие удобств комнаты")
    public ReservationPage checkFeatures(String... features) {
        for (String feature : features) {
            $(".col-lg-8").shouldHave(text(feature));
        }
        return this;
    }

    @Step("Проверить, что комната с описанием больше не отображается")
    public ReservationPage checkRoomNotDisplayed(String description) {
        $("body").shouldNotHave(text(description));
        return this;
    }
}
