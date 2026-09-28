package tests.ui;

import api.platform.PlatformApiClient;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import models.platform.LoginRequestModel;
import models.platform.RoomRequestModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.ReservationPage;
import tests.ui.testdata.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseTest {

    protected static final PlatformApiClient api = new PlatformApiClient();

    protected final TestData data = new TestData();
    protected final ReservationPage reservationPage = new ReservationPage();

    private final List<Integer> createdRoomIds = new ArrayList<>();
    private String token;

    @BeforeAll
    public static void setUp() {
        Configuration.baseUrl =
            System.getProperty("baseUrl", "https://automationintesting.online");
        String remoteUrl = System.getProperty("remoteUrl");
        if (remoteUrl != null && !remoteUrl.isBlank()) {
            Configuration.remote = remoteUrl;
        }
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        Configuration.pageLoadStrategy = "eager";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(List.of("--disable-dev-shm-usage", "--no-sandbox"));
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
            "enableVNC", true,
            "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        deleteCreatedRooms();
        if (WebDriverRunner.hasWebDriverStarted()) {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
            Attach.browserConsoleLogs();
            Attach.addVideo();
        }
        closeWebDriver();
    }

    protected int createRoomViaApi(RoomRequestModel body) {
        int roomId = api.rooms.createRoomAndGetId(adminToken(), body);
        createdRoomIds.add(roomId);
        return roomId;
    }

    protected void deleteRoomViaApi(int roomId) {
        api.rooms.deleteRoom(adminToken(), roomId);
        createdRoomIds.remove(Integer.valueOf(roomId));
    }

    private String adminToken() {
        if (token == null) {
            token = api.auth.login(new LoginRequestModel(data.adminUsername, data.adminPassword));
        }
        return token;
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
