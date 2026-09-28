package models.platform;

import java.util.List;

public record RoomRequestModel(
    String roomName,
    String type,
    boolean accessible,
    String description,
    String image,
    int roomPrice,
    List<String> features
) {
}
