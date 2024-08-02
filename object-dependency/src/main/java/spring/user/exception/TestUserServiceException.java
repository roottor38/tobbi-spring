package spring.user.exception;

public class TestUserServiceException extends RuntimeException {
    public TestUserServiceException(String message) {
        super(message);
    }

    public TestUserServiceException(String message, Throwable e) {
        super(message, e);
    }

}
