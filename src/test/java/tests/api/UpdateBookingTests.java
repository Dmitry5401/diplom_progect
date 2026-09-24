package tests.api;

import models.auth.AuthRequestModel;
import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.api.TestData.*;

@DisplayName("API restful-booker: Полное обновление бронирования. Метод PUT")
public class UpdateBookingTests extends TestBase{

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
}
