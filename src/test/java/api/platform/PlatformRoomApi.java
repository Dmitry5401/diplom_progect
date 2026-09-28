package api.platform;

import io.qameta.allure.Step;
import models.platform.RoomRequestModel;
import models.platform.RoomResponseModel;
import models.platform.RoomsResponseModel;

import static io.restassured.RestAssured.given;
import static specs.platform.PlatformSpec.platformRequestSpec;
import static specs.platform.PlatformSpec.roomCreatedResponseSpec;
import static specs.platform.PlatformSpec.roomDeletedResponseSpec;
import static specs.platform.PlatformSpec.roomsResponseSpec;

public class PlatformRoomApi {

    private static final String TOKEN_COOKIE = "token";

    @Step("Создание комнаты через API")
    public void createRoom(String token, RoomRequestModel body) {
        given(platformRequestSpec)
            .cookie(TOKEN_COOKIE, token)
            .body(body)
            .when()
            .post("/room")
            .then()
            .spec(roomCreatedResponseSpec);
    }

    @Step("Получение списка комнат через API")
    public RoomsResponseModel getRooms() {
        return given(platformRequestSpec)
            .when()
            .get("/room")
            .then()
            .spec(roomsResponseSpec)
            .extract()
            .as(RoomsResponseModel.class);
    }

    @Step("Поиск id комнаты по названию: {roomName}")
    public int findRoomIdByName(String roomName) {
        return getRooms().rooms().stream()
            .filter(room -> roomName.equals(room.roomName()))
            .map(RoomResponseModel::roomid)
            .findFirst()
            .orElseThrow(() ->
                new AssertionError("Комната с названием '" + roomName + "' не найдена через API"));
    }

    @Step("Создание комнаты и получение её id")
    public int createRoomAndGetId(String token, RoomRequestModel body) {
        createRoom(token, body);
        return findRoomIdByName(body.roomName());
    }

    @Step("Удаление комнаты через API по id: {roomId}")
    public void deleteRoom(String token, int roomId) {
        given(platformRequestSpec)
            .cookie(TOKEN_COOKIE, token)
            .when()
            .delete("/room/{id}", roomId)
            .then()
            .spec(roomDeletedResponseSpec);
    }

    @Step("Проверка через API, что комната с id {roomId} отсутствует")
    public boolean roomExists(int roomId) {
        return getRooms().rooms().stream()
            .anyMatch(room -> room.roomid() == roomId);
    }
}
