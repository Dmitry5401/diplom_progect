package tests.ui;

import io.qameta.allure.Feature;
import models.platform.RoomRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.ReservationPage;

import static io.qameta.allure.Allure.step;

@Feature("UI: отображение комнат на automationintesting.online")
@DisplayName("UI automationintesting.online: страница комнаты")
public class RoomUiTests extends TestBaseUi {

    private final ReservationPage reservationPage = new ReservationPage();

    @Test
    @DisplayName("Созданная через API комната корректно отображается на странице бронирования")
    public void createdRoomIsDisplayedTest() {
        RoomRequestModel room = newRoomBody();

        int roomId = createRoomViaApi(room);

        reservationPage.openReservation(roomId)
            .checkRoomDisplayed(room.type(), room.roomPrice(), room.description())
            .checkFeatures("WiFi", "TV");
    }

    @Test
    @DisplayName("Удалённая через API комната больше не отображается в UI")
    public void deletedRoomIsNotDisplayedTest() {
        RoomRequestModel room = newRoomBody();
        int roomId = createRoomViaApi(room);

        reservationPage.openReservation(roomId)
            .checkRoomDisplayed(room.type(), room.roomPrice(), room.description());

        deleteRoomViaApi(roomId);

        step("Повторное открытие страницы удалённой комнаты", () ->
            reservationPage.openReservation(roomId)
                .checkRoomNotDisplayed(room.description()));
    }
}
