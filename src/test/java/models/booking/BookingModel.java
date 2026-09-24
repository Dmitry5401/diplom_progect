package models.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingModel(
    String firstname,
    String lastname,
    Integer totalprice,
    Boolean depositpaid,
    BookingDatesModel bookingdates,
    String additionalneeds
) {
}
