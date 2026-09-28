package tests.ui.testdata;

import models.platform.RoomRequestModel;
import net.datafaker.Faker;

import java.util.List;

public class TestData {

    private final Faker faker = new Faker();

    public final String adminUsername = System.getProperty("adminUsername", "admin");
    public final String adminPassword = System.getProperty("adminPassword", "password");

    public RoomRequestModel roomToCreate() {
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
}
