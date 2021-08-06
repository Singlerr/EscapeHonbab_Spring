package io.github.escapehonbab.lang;

public class StaticMessage {
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String ERROR_TRANSACTION = "ERROR_TRANSACTION";
    public static final String SUCCESS_TRANSACTION = "SUCCESS_TRANSACTION";
    public static final String ERROR_NO_USER_FOUND = "ERROR_NO_USER_FOUND";
    public static final String UNSUPPORTED_DATA_TYPE = "UNSUPPORTED_DATA_TYPE";
    public static final String ERROR_LOGIN_FAILED = "ERROR_LOGIN_FAILED";
    public static final int RESP_SUCCESS = 200;
    public static final int RESP_FAILED = 400;

    public static class Verification {
        public static final String VERIFICATION_NOT_FOUND = "VERIFICATION_NOT_FOUND";
        public static final String CODE_MISMATCH = "CODE_MISMATCH";
        public static final String SUCCESS_VERIFICATION = "SUCCESS_VERIFICATION";
    }
}
