package kitchenpos.ordertable.exception;

public class OrderTableNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "테이블을 찾을 수 없습니다.";

    public OrderTableNotFoundException() {
        super(MESSAGE);
    }
}
