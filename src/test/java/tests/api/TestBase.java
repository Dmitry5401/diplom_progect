package tests.api;

import api.ApiClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static tests.api.TestData.requireAdminPassword;

public class TestBase {

    protected static final ApiClient api = new ApiClient();
    protected final TestDataGenerationSteps steps = new TestDataGenerationSteps(api);

    @BeforeAll
    public static void setUp() {
        requireAdminPassword();
        RestAssured.baseURI = System.getProperty(
            "baseUrl", "https://restful-booker.herokuapp.com");
    }

    @AfterEach
    public void cleanUpCreatedBookings() {
        steps.deleteCreatedBookings();
    }
}
