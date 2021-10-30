package kitchenpos.exception.order;

public class CannotPlaceAnOrderAsTableIsEmptyException extends RuntimeException {
    private static final String MESSAGE = "테이블이 주문을 받을 수 없어 주문을 할 수 없습니다.";

    public CannotPlaceAnOrderAsTableIsEmptyException() {
        super(MESSAGE);
    }
}
