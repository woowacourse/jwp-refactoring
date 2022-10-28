package kitchenpos.exception;

public class NotFoundOrderTableException extends NotFoundException {

    private static final String ERROR_MESSAGE = "존재하지 않는 주문 테이블입니다.";

    public NotFoundOrderTableException() {
        super(ERROR_MESSAGE);
    }
}
