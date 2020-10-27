package kitchenpos.application.exceptions;

public class NotExistedOrderException extends RuntimeException {
    public NotExistedOrderException() {
        super("존재하지 않는 Order입니다.");
    }
}
