package kitchenpos.exception;

public class EmptyTableOrderException extends IllegalArgumentException {

    private static final String MESSAGE = "빈 테이블에 주문을 등록할 수 없습니다.";

    public EmptyTableOrderException() {
        super(MESSAGE);
    }
}
