package api;

import io.qameta.allure.Step;
import models.auth.AuthRequestModel;
import models.auth.AuthResponseModel;

import static io.restassured.RestAssured.given;
import static specs.auth.AuthSpec.authRequestSpec;
import static specs.auth.AuthSpec.badCredentialsAuthResponseSpec;
import static specs.auth.AuthSpec.successfulAuthResponseSpec;

public class AuthApiClient {

    private static final String AUTH_PATH = "/auth";

    @Step("Авторизация и получение токена")
    public String createToken(AuthRequestModel authBody) {
        return given(authRequestSpec)
            .body(authBody)
            .when()
            .post(AUTH_PATH)
            .then()
            .spec(successfulAuthResponseSpec)
            .extract()
            .path("token");
    }

    @Step("Авторизация с неверными учётными данными")
    public AuthResponseModel createTokenWithBadCredentials(AuthRequestModel authBody) {
        return given(authRequestSpec)
            .body(authBody)
            .when()
            .post(AUTH_PATH)
            .then()
            .spec(badCredentialsAuthResponseSpec)
            .extract()
            .as(AuthResponseModel.class);
    }
}
