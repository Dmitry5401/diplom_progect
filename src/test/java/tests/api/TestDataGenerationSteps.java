package tests.api;

import api.ApiClient;
import io.qameta.allure.Step;
import models.auth.AuthRequestModel;
import models.booking.BookingDatesModel;
import models.booking.BookingModel;
import models.booking.CreateBookingResponseModel;
import models.booking.PatchBookingModel;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;

import static tests.api.TestData.ADMIN_PASSWORD;
import static tests.api.TestData.ADMIN_USERNAME;

public class TestDataGenerationSteps {

    private final ApiClient api;
    private final Faker faker = new Faker();
    private final List<Integer> createdBookingIds = new ArrayList<>();

    public TestDataGenerationSteps(ApiClient api) {
        this.api = api;
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

    @Step("Подготовка данных для частичного обновления")
    public PatchBookingModel newPatchBody() {
        return new PatchBookingModel(faker.name().firstName(), faker.name().lastName());
    }

    @Step("Создание бронирования")
    public CreateBookingResponseModel createBooking(BookingModel body) {
        CreateBookingResponseModel response = api.booking.createBooking(body);
        createdBookingIds.add(response.bookingid());
        return response;
    }

    public void deleteCreatedBookings() {
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
}
