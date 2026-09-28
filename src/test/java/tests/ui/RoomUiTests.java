package tests.ui;

import io.qameta.allure.Feature;
import models.platform.RoomRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

@Feature("UI: отображение комнат на automationintesting.online")
@DisplayName("UI automationintesting.online: страница комнаты")
public class RoomUiTests extends BaseTest {

    private RoomRequestModel room;
    private int roomId;

    @Test
    @DisplayName("Созданная через API комната корректно отображается на странице бронирования")
    void createdRoomIsDisplayed() {

        step("Пре-условие через API: создать комнату", () -> {
            room = data.roomToCreate();
            roomId = createRoomViaApi(room);
        });

        step("Открыть страницу бронирования комнаты", () -> {
            reservationPage.openReservation(roomId);
        });

        step("Проверить, что комната корректно отображается в UI", () -> {
            reservationPage.checkRoomDisplayed(room.type(), room.roomPrice(), room.description());
            reservationPage.checkFeatures("WiFi", "TV");
        });
    }

    @Test
    @DisplayName("Удалённая через API комната больше не отображается в UI")
    void deletedRoomIsNotDisplayed() {

        step("Пре-условие через API: создать комнату", () -> {
            room = data.roomToCreate();
            roomId = createRoomViaApi(room);
        });

        step("Проверить, что комната отображается в UI", () -> {
            reservationPage.openReservation(roomId);
            reservationPage.checkRoomDisplayed(room.type(), room.roomPrice(), room.description());
        });

        step("Пост-условие через API: удалить комнату (DELETE)", () -> {
            deleteRoomViaApi(roomId);
        });

        step("Проверить, что комната больше не отображается в UI", () -> {
            reservationPage.openReservation(roomId);
            reservationPage.checkRoomNotDisplayed(room.description());
        });
    }
}
