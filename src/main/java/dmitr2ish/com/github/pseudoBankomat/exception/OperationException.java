package dmitr2ish.com.github.pseudoBankomat.exception;

/**
 * @author dmitry ishmitov
 * @date 10/23/22
 * TODO description
 */
public class OperationException extends Throwable {
    private final String message;

    public OperationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
