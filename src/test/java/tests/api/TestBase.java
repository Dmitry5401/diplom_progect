package tests.api;

import api.ApiClient;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import models.booking.BookingDatesModel;
import models.booking.BookingModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    private final Faker faker = new Faker();

    protected static final ApiClient api = new ApiClient();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = System.getProperty(
            "baseUrl", "https://restful-booker.herokuapp.com");
    }

    @Step("Подготовка данных для бронирования")
    public BookingModel newBookingBody() {
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
