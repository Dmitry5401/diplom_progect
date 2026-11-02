package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ReservationPage {

    private static final Duration FORM_TIMEOUT = Duration.ofSeconds(10);

    private final SelenideElement
        roomTitle = $("h1"),
        bookingCard = $(".booking-card"),
        roomDetails = $(".col-lg-8"),
        page = $("body"),
        openBookingButton = $("#doReservation"),
        firstName = $(".room-firstname"),
        lastName = $(".room-lastname"),
        guestEmail = $(".room-email"),
        guestPhone = $(".room-phone"),
        submitBooking = $(".booking-card button.btn-primary"),
        validationAlert = $(".alert-danger");

    public void openReservation(int roomId, String checkin, String checkout) {
        open("/reservation/" + roomId + "?checkin=" + checkin + "&checkout=" + checkout);
    }

    public void openBookingForm() {
        openBookingButton.shouldBe(visible, FORM_TIMEOUT).click();
        firstName.shouldBe(visible);
    }

    @Step("Заполнить имя гостя: {firstname}")
    public ReservationPage setFirstName(String firstname) {
        fill(firstName, firstname);
        return this;
    }

    @Step("Заполнить фамилию гостя: {lastname}")
    public ReservationPage setLastName(String lastname) {
        fill(lastName, lastname);
        return this;
    }

    @Step("Заполнить email гостя: {email}")
    public ReservationPage setEmail(String email) {
        fill(guestEmail, email);
        return this;
    }

    @Step("Заполнить телефон гостя: {phone}")
    public ReservationPage setPhone(String phone) {
        fill(guestPhone, phone);
        return this;
    }

    @Step("Нажать кнопку бронирования")
    public ReservationPage clickSubmit() {
        submitBooking.click();
        return this;
    }

    private void fill(SelenideElement field, String text) {
        field.setValue(text);
        if (text.isEmpty()) {
            field.shouldBe(empty);
        } else {
            field.shouldHave(value(text));
        }
    }

    public void checkValidationError(String error) {
        validationAlert.shouldBe(visible, FORM_TIMEOUT).shouldHave(text(error));
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
