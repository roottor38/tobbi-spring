package spring.user.exception;

public class DuplicationUserIdException extends RuntimeException {

  public DuplicationUserIdException(Throwable cause) {
    super(cause);
  }

}
