package tests.api;

import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.api.TestData.NONEXISTENT_BOOKING_ID;
import static tests.api.TestData.NOT_FOUND_BODY;

@DisplayName("API restful-booker: Получение бронирования по idю Метод GET")
public class GetBookingTests extends TestBase {

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
}
