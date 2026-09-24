package specs.booking;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class BookingSpec {

    public static RequestSpecification bookingRequestSpec = baseRequestSpec;

    public static ResponseSpecification createBookingResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath(
            "schemas/booking/create_booking_response_schema.json"))
        .expectBody("bookingid", notNullValue())
        .build();

    public static ResponseSpecification getBookingResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath(
            "schemas/booking/booking_response_schema.json"))
        .expectBody("firstname", notNullValue())
        .build();

    public static ResponseSpecification updateBookingResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath(
            "schemas/booking/booking_response_schema.json"))
        .expectBody("firstname", notNullValue())
        .build();

    public static ResponseSpecification bookingNotFoundResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(404)
        .build();

    public static ResponseSpecification bookingBadRequestResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(500)
        .build();

    public static ResponseSpecification bookingForbiddenResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(403)
        .build();
}
