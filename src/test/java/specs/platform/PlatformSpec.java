package specs.platform;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static allure.CustomAllureListener.withCustomTemplate;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.notNullValue;

public class PlatformSpec {

    public static final String PLATFORM_URL =
        System.getProperty("platformUrl", "https://automationintesting.online");

    public static RequestSpecification platformRequestSpec = with()
        .filter(withCustomTemplate())
        .log().all()
        .baseUri(PLATFORM_URL)
        .basePath("/api")
        .contentType(JSON)
        .accept(JSON);

    public static ResponseSpecification loginResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(200)
        .expectBody("token", notNullValue())
        .build();

    public static ResponseSpecification roomCreatedResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(200)
        .expectBody("success", notNullValue())
        .build();

    public static ResponseSpecification roomsResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(200)
        .expectBody("rooms", notNullValue())
        .build();

    public static ResponseSpecification roomDeletedResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(202)
        .build();
}
