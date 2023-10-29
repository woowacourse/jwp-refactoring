package exception;

import org.springframework.http.HttpStatus;

public class InvalidNameException extends HttpException {

    public InvalidNameException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
