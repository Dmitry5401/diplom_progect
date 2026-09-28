package api.platform;

import io.qameta.allure.Step;
import models.platform.LoginRequestModel;

import static io.restassured.RestAssured.given;
import static specs.platform.PlatformSpec.loginResponseSpec;
import static specs.platform.PlatformSpec.platformRequestSpec;

public class PlatformAuthApi {

    @Step("Авторизация в админке и получение токена")
    public String login(LoginRequestModel loginBody) {
        return given(platformRequestSpec)
            .body(loginBody)
            .when()
            .post("/auth/login")
            .then()
            .spec(loginResponseSpec)
            .extract()
            .path("token");
    }
}
