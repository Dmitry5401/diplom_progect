package specs.booking;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;
import static specs.BaseSpec.baseResponseSpec;

public class BookingSpec {

    public static RequestSpecification bookingRequestSpec = baseRequestSpec;

    public static ResponseSpecification createBookingResponseSpec = new ResponseSpecBuilder()
        .addResponseSpecification(baseResponseSpec)
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath(
            "schemas/booking/create_booking_response_schema.json"))
        .expectBody("bookingid", notNullValue())
        .build();

    public static ResponseSpecification getBookingResponseSpec = new ResponseSpecBuilder()
        .addResponseSpecification(baseResponseSpec)
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath(
            "schemas/booking/booking_response_schema.json"))
        .expectBody("firstname", notNullValue())
        .build();

    public static ResponseSpecification updateBookingResponseSpec = new ResponseSpecBuilder()
        .addResponseSpecification(baseResponseSpec)
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath(
            "schemas/booking/booking_response_schema.json"))
        .expectBody("firstname", notNullValue())
        .build();

    public static ResponseSpecification patchBookingResponseSpec = new ResponseSpecBuilder()
        .addResponseSpecification(baseResponseSpec)
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath(
            "schemas/booking/booking_response_schema.json"))
        .expectBody("firstname", notNullValue())
        .build();

    public static ResponseSpecification deleteBookingResponseSpec = new ResponseSpecBuilder()
        .addResponseSpecification(baseResponseSpec)
        .expectStatusCode(201)
        .build();

    public static ResponseSpecification bookingNotFoundResponseSpec = new ResponseSpecBuilder()
        .addResponseSpecification(baseResponseSpec)
        .expectStatusCode(404)
        .build();

    public static ResponseSpecification bookingBadRequestResponseSpec = new ResponseSpecBuilder()
        .addResponseSpecification(baseResponseSpec)
        .expectStatusCode(500)
        .build();

    public static ResponseSpecification bookingForbiddenResponseSpec = new ResponseSpecBuilder()
        .addResponseSpecification(baseResponseSpec)
        .expectStatusCode(403)
        .build();
}
