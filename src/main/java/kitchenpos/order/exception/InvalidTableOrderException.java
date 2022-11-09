package kitchenpos.order.exception;

public class InvalidTableOrderException extends IllegalArgumentException {

    private static final String MESSAGE = "테이블이 존재하지 않거나 비어있는 테이블입니다.";

    public InvalidTableOrderException() {
        super(MESSAGE);
    }
}
