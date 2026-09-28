package models.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RoomResponseModel(
    int roomid,
    String roomName,
    String type,
    boolean accessible,
    String description,
    String image,
    int roomPrice,
    List<String> features
) {
}
