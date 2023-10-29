package exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderException extends HttpException {

    public InvalidOrderException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
