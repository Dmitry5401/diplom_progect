package models.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBookingResponseModel(Integer bookingid, BookingModel booking) {
}
