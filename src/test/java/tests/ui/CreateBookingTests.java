package tests.ui;

import io.qameta.allure.Step;
import models.booking.BookingDatesModel;
import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.ui.TestData.SERVER_ERROR_BODY;

@DisplayName("API restful-booker: создание бронирования. Метод POST")
public class CreateBookingTests extends TestBase{

    private final Faker faker = new Faker();

    @Test
    @DisplayName("POST: создание бронирования (позитивный сценарий, 200)")
    public void createBookingTest() {

        BookingModel bookingData = newBookingBody();

        CreateBookingResponseModel response = api.booking.createBooking(bookingData);

        step("Проверка созданного бронирования", () -> {
            assertThat(response.bookingid()).isPositive();
            assertThat(response.booking().firstname()).isEqualTo(bookingData.firstname());
            assertThat(response.booking().lastname()).isEqualTo(bookingData.lastname());
            assertThat(response.booking().totalprice()).isEqualTo(bookingData.totalprice());
            assertThat(response.booking().depositpaid()).isEqualTo(bookingData.depositpaid());
            assertThat(response.booking().bookingdates()).isEqualTo(bookingData.bookingdates());
            assertThat(response.booking().additionalneeds()).isEqualTo(bookingData.additionalneeds());
        });
    }

    @Test
    @DisplayName("POST: создание бронирования с некорректным телом (ошибка, 500)")
    public void createBookingWithInvalidBodyTest() {
        String response = api.booking.createBookingWithInvalidBody("{}");

        step("Проверка ответа об ошибке сервера", () ->
            assertThat(response).contains(SERVER_ERROR_BODY)
        );
    }

    @Step("Подготовка данных для бронирования")
    private BookingModel newBookingBody() {
        return new BookingModel(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.number().numberBetween(50, 5000),
            faker.bool().bool(),
            new BookingDatesModel("2024-01-01", "2024-12-31"),
            faker.options().option("Breakfast", "Dinner", "Late checkout", "Extra towels")
        );
    }
}
