package tests.api;

public final class TestData {

    private TestData() {
    }

    public static final String ADMIN_USERNAME = System.getProperty("adminUsername", "admin");
    public static final String ADMIN_PASSWORD = System.getProperty("adminPassword", "password123");
    public static final String WRONG_PASSWORD = "wrong_password";

    public static final int NONEXISTENT_BOOKING_ID = 99999999;

    public static final String NOT_FOUND_BODY = "Not Found";
    public static final String FORBIDDEN_BODY = "Forbidden";
    public static final String SERVER_ERROR_BODY = "Internal Server Error";
    public static final String BAD_CREDENTIALS_REASON = "Bad credentials";
}
