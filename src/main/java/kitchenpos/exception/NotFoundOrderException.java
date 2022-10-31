package kitchenpos.exception;

public class NotFoundOrderException extends NotFoundException {

    private static final String ERROR_MESSAGE = "존재하지 않는 주문입니다.";

    public NotFoundOrderException() {
        super(ERROR_MESSAGE);
    }
}
