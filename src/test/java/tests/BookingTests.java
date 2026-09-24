package tests;

import io.qameta.allure.Step;
import models.auth.AuthRequestModel;
import models.booking.BookingDatesModel;
import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

@DisplayName("API restful-booker: бронирования")
public class BookingTests extends TestBase {

    private final Faker faker = new Faker();

    // ---------- POST /booking ----------

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

    // ---------- GET /booking/{id} ----------

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

    // ---------- PUT /booking/{id} ----------

    @Test
    @DisplayName("PUT: полное обновление бронирования (позитивный сценарий, 200)")
    public void updateBookingTest() {
        CreateBookingResponseModel createdBooking = api.booking.createBooking(newBookingBody());
        String token = api.auth.createToken(new AuthRequestModel(ADMIN_USERNAME, ADMIN_PASSWORD));
        BookingModel updateData = newBookingBody();

        BookingModel response =
            api.booking.updateBooking(token, createdBooking.bookingid(), updateData);

        step("Проверка обновлённых данных бронирования", () -> {
            assertThat(response.firstname()).isEqualTo(updateData.firstname());
            assertThat(response.lastname()).isEqualTo(updateData.lastname());
            assertThat(response.totalprice()).isEqualTo(updateData.totalprice());
            assertThat(response.depositpaid()).isEqualTo(updateData.depositpaid());
            assertThat(response.bookingdates()).isEqualTo(updateData.bookingdates());
            assertThat(response.additionalneeds()).isEqualTo(updateData.additionalneeds());
        });
    }

    @Test
    @DisplayName("PUT: обновление бронирования без авторизации (ошибка, 403)")
    public void updateBookingWithoutTokenTest() {
        CreateBookingResponseModel createdBooking = api.booking.createBooking(newBookingBody());

        String response =
            api.booking.updateBookingWithoutToken(createdBooking.bookingid(), newBookingBody());

        step("Проверка ответа 'Forbidden'", () ->
            assertThat(response).contains(FORBIDDEN_BODY)
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
