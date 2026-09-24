package api;

import io.qameta.allure.Step;
import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import models.booking.PatchBookingModel;

import static io.restassured.RestAssured.given;
import static specs.booking.BookingSpec.bookingBadRequestResponseSpec;
import static specs.booking.BookingSpec.bookingForbiddenResponseSpec;
import static specs.booking.BookingSpec.bookingNotFoundResponseSpec;
import static specs.booking.BookingSpec.bookingRequestSpec;
import static specs.booking.BookingSpec.createBookingResponseSpec;
import static specs.booking.BookingSpec.deleteBookingResponseSpec;
import static specs.booking.BookingSpec.getBookingResponseSpec;
import static specs.booking.BookingSpec.patchBookingResponseSpec;
import static specs.booking.BookingSpec.updateBookingResponseSpec;

public class BookingApiClient {

    private static final String BOOKING_PATH = "/booking";
    private static final String BOOKING_BY_ID_PATH = "/booking/{id}";
    private static final String TOKEN_COOKIE = "token";

    @Step("Создание бронирования")
    public CreateBookingResponseModel createBooking(BookingModel body) {
        return given(bookingRequestSpec)
            .body(body)
            .when()
            .post(BOOKING_PATH)
            .then()
            .spec(createBookingResponseSpec)
            .extract()
            .as(CreateBookingResponseModel.class);
    }

    @Step("Создание бронирования с некорректным телом запроса")
    public String createBookingWithInvalidBody(Object body) {
        return given(bookingRequestSpec)
            .body(body)
            .when()
            .post(BOOKING_PATH)
            .then()
            .spec(bookingBadRequestResponseSpec)
            .extract()
            .asString();
    }

    @Step("Получение бронирования по id")
    public BookingModel getBooking(int id) {
        return given(bookingRequestSpec)
            .when()
            .get(BOOKING_BY_ID_PATH, id)
            .then()
            .spec(getBookingResponseSpec)
            .extract()
            .as(BookingModel.class);
    }

    @Step("Получение несуществующего бронирования")
    public String getMissingBooking(int id) {
        return given(bookingRequestSpec)
            .when()
            .get(BOOKING_BY_ID_PATH, id)
            .then()
            .spec(bookingNotFoundResponseSpec)
            .extract()
            .asString();
    }

    @Step("Полное обновление бронирования")
    public BookingModel updateBooking(String token, int id, BookingModel body) {
        return given(bookingRequestSpec)
            .cookie(TOKEN_COOKIE, token)
            .body(body)
            .when()
            .put(BOOKING_BY_ID_PATH, id)
            .then()
            .spec(updateBookingResponseSpec)
            .extract()
            .as(BookingModel.class);
    }

    @Step("Обновление бронирования без токена авторизации")
    public String updateBookingWithoutToken(int id, BookingModel body) {
        return given(bookingRequestSpec)
            .body(body)
            .when()
            .put(BOOKING_BY_ID_PATH, id)
            .then()
            .spec(bookingForbiddenResponseSpec)
            .extract()
            .asString();
    }

    @Step("Частичное обновление бронирования")
    public BookingModel patchBooking(String token, int id, PatchBookingModel body) {
        return given(bookingRequestSpec)
            .cookie(TOKEN_COOKIE, token)
            .body(body)
            .when()
            .patch(BOOKING_BY_ID_PATH, id)
            .then()
            .spec(patchBookingResponseSpec)
            .extract()
            .as(BookingModel.class);
    }

    @Step("Частичное обновление бронирования без токена авторизации")
    public String patchBookingWithoutToken(int id, PatchBookingModel body) {
        return given(bookingRequestSpec)
            .body(body)
            .when()
            .patch(BOOKING_BY_ID_PATH, id)
            .then()
            .spec(bookingForbiddenResponseSpec)
            .extract()
            .asString();
    }

    @Step("Удаление бронирования")
    public void deleteBooking(String token, int id) {
        given(bookingRequestSpec)
            .cookie(TOKEN_COOKIE, token)
            .when()
            .delete(BOOKING_BY_ID_PATH, id)
            .then()
            .spec(deleteBookingResponseSpec);
    }

    @Step("Удаление бронирования без токена авторизации")
    public String deleteBookingWithoutToken(int id) {
        return given(bookingRequestSpec)
            .when()
            .delete(BOOKING_BY_ID_PATH, id)
            .then()
            .spec(bookingForbiddenResponseSpec)
            .extract()
            .asString();
    }
}
