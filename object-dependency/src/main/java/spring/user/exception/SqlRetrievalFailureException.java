package spring.user.exception;

public class SqlRetrievalFailureException extends RuntimeException {

  public SqlRetrievalFailureException(String message) {
    super(message);
  }

    public SqlRetrievalFailureException(Exception e) {
        super(e);
    }

  public SqlRetrievalFailureException(String message, Throwable cause) {
    super(message, cause);
  }

}
