package tests.ui;

import api.platform.PlatformApiClient;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import models.platform.LoginRequestModel;
import models.platform.RoomRequestModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static specs.platform.PlatformSpec.PLATFORM_URL;

public class TestBaseUi {

    protected static final String ADMIN_USERNAME = System.getProperty("adminUsername", "admin");
    protected static final String ADMIN_PASSWORD = System.getProperty("adminPassword", "password");

    protected static final PlatformApiClient api = new PlatformApiClient();

    protected final Faker faker = new Faker();

    private final List<Integer> createdRoomIds = new ArrayList<>();
    private String token;

    @BeforeAll
    public static void setUpBrowser() {
        Configuration.baseUrl = PLATFORM_URL;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
        Configuration.pageLoadStrategy = "eager";
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        Configuration.browserCapabilities = options;
    }

    @BeforeEach
    public void addAllureListener() {
        SelenideLogger.addListener("allure", new AllureSelenide()
            .screenshots(true)
            .savePageSource(true));
    }

    @AfterEach
    public void cleanUp() {
        deleteCreatedRooms();
        SelenideLogger.removeListener("allure");
        closeWebDriver();
    }

    @Step("Авторизация в админке")
    protected String adminToken() {
        if (token == null) {
            token = api.auth.login(new LoginRequestModel(ADMIN_USERNAME, ADMIN_PASSWORD));
        }
        return token;
    }

    @Step("Пре-условие: создание комнаты через API")
    protected int createRoomViaApi(RoomRequestModel body) {
        int roomId = api.rooms.createRoomAndGetId(adminToken(), body);
        createdRoomIds.add(roomId);
        return roomId;
    }

    @Step("Пост-условие: удаление комнаты через API")
    protected void deleteRoomViaApi(int roomId) {
        api.rooms.deleteRoom(adminToken(), roomId);
        createdRoomIds.remove(Integer.valueOf(roomId));
    }

    @Step("Подготовка данных для комнаты")
    protected RoomRequestModel newRoomBody() {
        String roomName = String.valueOf(faker.number().numberBetween(100000, 999999));
        return new RoomRequestModel(
            roomName,
            faker.options().option("Single", "Double", "Twin", "Suite"),
            true,
            "UI autotest room " + roomName + " " + faker.lorem().sentence(),
            "/images/room2.jpg",
            faker.number().numberBetween(50, 500),
            List.of("WiFi", "TV", "Safe")
        );
    }

    private void deleteCreatedRooms() {
        if (createdRoomIds.isEmpty()) {
            return;
        }
        for (int roomId : new ArrayList<>(createdRoomIds)) {
            try {
                api.rooms.deleteRoom(adminToken(), roomId);
            } catch (AssertionError ignored) {
                // комната уже удалена в тесте или недоступна
            }
        }
        createdRoomIds.clear();
    }
}
