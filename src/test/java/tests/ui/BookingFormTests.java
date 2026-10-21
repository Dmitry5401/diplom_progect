package tests.ui;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;

@Feature("UI: валидация формы бронирования на automationintesting.online")
@DisplayName("UI automationintesting.online: форма бронирования")
public class BookingFormTests extends BaseTest {

    private static final String VALID_FIRSTNAME = "James";
    private static final String VALID_LASTNAME = "Smith";
    private static final String VALID_EMAIL = "guest@example.com";
    private static final String VALID_PHONE = "012345678901";

    private int roomId;

    static Stream<Arguments> invalidGuests() {
        return Stream.of(
            Arguments.of(
                "пустое имя",
                "",
                VALID_LASTNAME,
                VALID_EMAIL,
                VALID_PHONE,
                "Firstname should not be blank"
            ),
            Arguments.of(
                "имя короче 3 символов",
                "Jo",
                VALID_LASTNAME,
                VALID_EMAIL,
                VALID_PHONE,
                "size must be between 3 and 18"
            ),
            Arguments.of(
                "пустая фамилия",
                VALID_FIRSTNAME,
                "",
                VALID_EMAIL,
                VALID_PHONE,
                "Lastname should not be blank"
            ),
            Arguments.of(
                "некорректный email",
                VALID_FIRSTNAME,
                VALID_LASTNAME,
                "not-an-email",
                VALID_PHONE,
                "must be a well-formed email address"
            ),
            Arguments.of(
                "телефон короче 11 символов",
                VALID_FIRSTNAME,
                VALID_LASTNAME,
                VALID_EMAIL,
                "1234567890",
                "size must be between 11 and 21"
            )
        );
    }

    @AfterEach
    void deleteRoom() {
        if (roomId > 0) {
            deleteRoomViaApi(roomId);
            roomId = 0;
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidGuests")
    @DisplayName("Форма бронирования показывает ошибку валидации данных гостя")
    void invalidGuestShowsValidationError(String caseName, String firstname, String lastname,
                                          String email, String phone, String expectedError) {
        String checkin = LocalDate.now().plusDays(7).toString();
        String checkout = LocalDate.now().plusDays(8).toString();

        step("Пре-условие через API: создать комнату", () -> {
            roomId = createRoomViaApi(data.roomToCreate());
        });

        step("Открыть страницу бронирования: заезд " + checkin + ", выезд " + checkout, () ->
            reservationPage.openReservation(roomId, checkin, checkout)
        );

        step("Открыть форму бронирования", () ->
            reservationPage.openBookingForm()
        );

        step("Отправить данные гостя: " + caseName, () ->
            reservationPage.submitGuest(firstname, lastname, email, phone)
        );

        step("Проверить ошибку валидации", () ->
            reservationPage.checkValidationError(expectedError)
        );
    }
}
