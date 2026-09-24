package specs.auth;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class AuthSpec {

    public static RequestSpecification authRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulAuthResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(200)
        .expectBody(matchesJsonSchemaInClasspath("schemas/auth/auth_token_response_schema.json"))
        .expectBody("token", notNullValue())
        .build();

    public static ResponseSpecification badCredentialsAuthResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(200)
        .expectBody("reason", notNullValue())
        .build();
}
