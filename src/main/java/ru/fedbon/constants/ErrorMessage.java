package ru.fedbon.constants;

public class ErrorMessage {

    public static final String NOT_FOUND = "NOT_FOUND";

    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    public static final String USER_REQUEST_NOT_FOUND = "No requests found for user with id: ";

    public static final String DRAFT_USER_REQUEST_NOT_FOUND = "No draft request found with id: ";

    public static final String PENDING_USER_REQUEST_NOT_FOUND = "No pending requests found";

    public static final String ROLE_ALREADY_EXISTS = "Role already exists for user with id: ";

    public static final String INVALID_TOKEN = "Invalid token";

    public static final String USER_NOT_FOUND = "No user found with username: ";

    private ErrorMessage() {

    }
}
