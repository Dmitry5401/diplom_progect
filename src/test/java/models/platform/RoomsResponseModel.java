package models.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RoomsResponseModel(List<RoomResponseModel> rooms) {
}
