package tests.api;

import api.ApiClient;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import models.auth.AuthRequestModel;
import models.booking.BookingDatesModel;
import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import models.booking.PatchBookingModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

import static tests.api.TestData.ADMIN_PASSWORD;
import static tests.api.TestData.ADMIN_USERNAME;

public class TestBase {

    protected static final ApiClient api = new ApiClient();
    protected final Faker faker = new Faker();

    private final List<Integer> createdBookingIds = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = System.getProperty(
            "baseUrl", "https://restful-booker.herokuapp.com");
    }

    @AfterEach
    public void cleanUpCreatedBookings() {
        if (createdBookingIds.isEmpty()) {
            return;
        }
        String token = api.auth.createToken(new AuthRequestModel(ADMIN_USERNAME, ADMIN_PASSWORD));
        for (int id : createdBookingIds) {
            try {
                api.booking.deleteBooking(token, id);
            } catch (AssertionError ignored) {
                // бронирование уже удалено в тесте или недоступно
            }
        }
        createdBookingIds.clear();
    }

    @Step("Создание бронирования")
    protected CreateBookingResponseModel createBooking(BookingModel body) {
        CreateBookingResponseModel response = api.booking.createBooking(body);
        createdBookingIds.add(response.bookingid());
        return response;
    }

    @Step("Подготовка данных для бронирования")
    protected BookingModel newBookingBody() {
        return new BookingModel(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.number().numberBetween(50, 5000),
            faker.bool().bool(),
            new BookingDatesModel("2024-01-01", "2024-12-31"),
            faker.options().option("Breakfast", "Dinner", "Late checkout", "Extra towels")
        );
    }

    @Step("Подготовка данных для частичного обновления")
    protected PatchBookingModel newPatchBody() {
        return new PatchBookingModel(faker.name().firstName(), faker.name().lastName());
    }
}
