package specs;

import io.restassured.specification.RequestSpecification;

import static allure.CustomAllureListener.withCustomTemplate;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class BaseSpec {

    // restful-booker отвечает "418 I'm a teapot", если Accept не равен строго
    // application/json, поэтому задаём заголовок явно, а не через ContentType.JSON.
    public static RequestSpecification baseRequestSpec = with()
        .filter(withCustomTemplate())
        .log().all()
        .contentType(JSON)
        .accept("application/json");
}
