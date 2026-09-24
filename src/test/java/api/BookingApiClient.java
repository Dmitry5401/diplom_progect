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

    @Step("Создание бронирования")
    public CreateBookingResponseModel createBooking(BookingModel body) {
        return given(bookingRequestSpec)
            .body(body)
            .when()
            .post("/booking")
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
            .post("/booking")
            .then()
            .spec(bookingBadRequestResponseSpec)
            .extract()
            .asString();
    }

    @Step("Получение бронирования по id")
    public BookingModel getBooking(int id) {
        return given(bookingRequestSpec)
            .when()
            .get("/booking/{id}", id)
            .then()
            .spec(getBookingResponseSpec)
            .extract()
            .as(BookingModel.class);
    }

    @Step("Получение несуществующего бронирования")
    public String getMissingBooking(int id) {
        return given(bookingRequestSpec)
            .when()
            .get("/booking/{id}", id)
            .then()
            .spec(bookingNotFoundResponseSpec)
            .extract()
            .asString();
    }

    @Step("Полное обновление бронирования")
    public BookingModel updateBooking(String token, int id, BookingModel body) {
        return given(bookingRequestSpec)
            .header("Cookie", "token=" + token)
            .body(body)
            .when()
            .put("/booking/{id}", id)
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
            .put("/booking/{id}", id)
            .then()
            .spec(bookingForbiddenResponseSpec)
            .extract()
            .asString();
    }

    @Step("Частичное обновление бронирования")
    public BookingModel patchBooking(String token, int id, PatchBookingModel body) {
        return given(bookingRequestSpec)
            .header("Cookie", "token=" + token)
            .body(body)
            .when()
            .patch("/booking/{id}", id)
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
            .patch("/booking/{id}", id)
            .then()
            .spec(bookingForbiddenResponseSpec)
            .extract()
            .asString();
    }

    @Step("Удаление бронирования")
    public void deleteBooking(String token, int id) {
        given(bookingRequestSpec)
            .header("Cookie", "token=" + token)
            .when()
            .delete("/booking/{id}", id)
            .then()
            .spec(deleteBookingResponseSpec);
    }

    @Step("Удаление бронирования без токена авторизации")
    public String deleteBookingWithoutToken(int id) {
        return given(bookingRequestSpec)
            .when()
            .delete("/booking/{id}", id)
            .then()
            .spec(bookingForbiddenResponseSpec)
            .extract()
            .asString();
    }
}
