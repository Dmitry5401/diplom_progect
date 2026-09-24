package tests.api;

import models.auth.AuthRequestModel;
import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import models.booking.PatchBookingModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.api.TestData.*;

@DisplayName("API restful-booker: Частичное обновление бронирования. Метод PATCH")
public class PatchBookingTests extends TestBase {

    private final Faker faker = new Faker();

    @Test
    @DisplayName("PATCH: частичное обновление бронирования (позитивный сценарий, 200)")
    public void patchBookingTest() {
        CreateBookingResponseModel createdBooking = api.booking.createBooking(newBookingBody());
        BookingModel original = createdBooking.booking();
        String token = api.auth.createToken(new AuthRequestModel(ADMIN_USERNAME, ADMIN_PASSWORD));
        PatchBookingModel patchData =
            new PatchBookingModel(faker.name().firstName(), faker.name().lastName());

        BookingModel response =
            api.booking.patchBooking(token, createdBooking.bookingid(), patchData);

        step("Проверка, что изменились только имя и фамилия", () -> {
            assertThat(response.firstname()).isEqualTo(patchData.firstname());
            assertThat(response.lastname()).isEqualTo(patchData.lastname());
            assertThat(response.totalprice()).isEqualTo(original.totalprice());
            assertThat(response.depositpaid()).isEqualTo(original.depositpaid());
            assertThat(response.bookingdates()).isEqualTo(original.bookingdates());
            assertThat(response.additionalneeds()).isEqualTo(original.additionalneeds());
        });
    }

    @Test
    @DisplayName("PATCH: частичное обновление без авторизации (ошибка, 403)")
    public void patchBookingWithoutTokenTest() {
        CreateBookingResponseModel createdBooking = api.booking.createBooking(newBookingBody());
        PatchBookingModel patchData =
            new PatchBookingModel(faker.name().firstName(), faker.name().lastName());

        String response =
            api.booking.patchBookingWithoutToken(createdBooking.bookingid(), patchData);

        step("Проверка ответа 'Forbidden'", () ->
            assertThat(response).contains(FORBIDDEN_BODY)
        );
    }
}
