package models.booking;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PatchBookingModel(String firstname, String lastname) {
}
