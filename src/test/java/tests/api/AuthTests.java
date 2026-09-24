package tests.api;

import models.auth.AuthRequestModel;
import models.auth.AuthResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.api.TestData.*;

@DisplayName("API restful-booker: Авторизация. Метод POST /auth")
public class AuthTests extends TestBase {

    @Test
    @DisplayName("POST /auth: получение токена с валидными данными (позитивный сценарий, 200)")
    public void createTokenTest() {
        String token = api.auth.createToken(new AuthRequestModel(ADMIN_USERNAME, ADMIN_PASSWORD));

        step("Проверка, что токен получен", () ->
            assertThat(token).isNotBlank()
        );
    }

    @Test
    @DisplayName("POST /auth: авторизация с неверным паролем (ошибка, Bad credentials)")
    public void createTokenWithBadCredentialsTest() {
        AuthResponseModel response = api.auth.createTokenWithBadCredentials(
            new AuthRequestModel(ADMIN_USERNAME, WRONG_PASSWORD));

        step("Проверка, что токен не выдан и вернулась причина ошибки", () -> {
            assertThat(response.token()).isNull();
            assertThat(response.reason()).isEqualTo(BAD_CREDENTIALS_REASON);
        });
    }
}
