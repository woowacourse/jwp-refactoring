package kitchenpos.domain.exception;

public class NotExistMenuException extends RuntimeException{
    
    public NotExistMenuException(final String message) {
        super(message);
    }
}
