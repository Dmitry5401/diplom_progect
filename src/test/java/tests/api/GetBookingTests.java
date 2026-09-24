package tests.api;

import io.qameta.allure.Step;
import models.booking.BookingDatesModel;
import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.api.TestData.NONEXISTENT_BOOKING_ID;
import static tests.api.TestData.NOT_FOUND_BODY;

@DisplayName("API restful-booker: Получение бронирования по idю Метод GET")
public class GetBookingTests extends TestBase {
    private final Faker faker = new Faker();

    @Test
    @DisplayName("GET: получение бронирования по id (позитивный сценарий, 200)")
    public void getBookingByIdTest() {
        CreateBookingResponseModel createdBooking = api.booking.createBooking(newBookingBody());
        BookingModel expected = createdBooking.booking();

        BookingModel response = api.booking.getBooking(createdBooking.bookingid());

        step("Проверка данных бронирования", () -> {
            assertThat(response.firstname()).isEqualTo(expected.firstname());
            assertThat(response.lastname()).isEqualTo(expected.lastname());
            assertThat(response.totalprice()).isEqualTo(expected.totalprice());
            assertThat(response.depositpaid()).isEqualTo(expected.depositpaid());
            assertThat(response.bookingdates()).isEqualTo(expected.bookingdates());
            assertThat(response.additionalneeds()).isEqualTo(expected.additionalneeds());
        });
    }

    @Test
    @DisplayName("GET: получение несуществующего бронирования (ошибка, 404)")
    public void getNonexistentBookingTest() {
        String response = api.booking.getMissingBooking(NONEXISTENT_BOOKING_ID);

        step("Проверка ответа 'Not Found'", () ->
            assertThat(response).contains(NOT_FOUND_BODY)
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
