package tests.api;

import models.auth.AuthRequestModel;
import models.booking.CreateBookingResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.api.TestData.*;

@DisplayName("API restful-booker: Удаление бронирования. Метод DELETE")
public class DeleteBookingTests extends TestBase {

    @Test
    @DisplayName("DELETE: удаление бронирования (позитивный сценарий, 201)")
    public void deleteBookingTest() {
        CreateBookingResponseModel createdBooking = api.booking.createBooking(newBookingBody());
        String token = api.auth.createToken(new AuthRequestModel(ADMIN_USERNAME, ADMIN_PASSWORD));

        api.booking.deleteBooking(token, createdBooking.bookingid());

        String response = api.booking.getMissingBooking(createdBooking.bookingid());

        step("Проверка, что бронирование удалено (Not Found)", () ->
            assertThat(response).contains(NOT_FOUND_BODY)
        );
    }

    @Test
    @DisplayName("DELETE: удаление бронирования без авторизации (ошибка, 403)")
    public void deleteBookingWithoutTokenTest() {
        CreateBookingResponseModel createdBooking = api.booking.createBooking(newBookingBody());

        String response = api.booking.deleteBookingWithoutToken(createdBooking.bookingid());

        step("Проверка ответа 'Forbidden'", () ->
            assertThat(response).contains(FORBIDDEN_BODY)
        );
    }
}
