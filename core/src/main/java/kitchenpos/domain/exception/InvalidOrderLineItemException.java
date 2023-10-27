package kitchenpos.domain.exception;

public class InvalidOrderLineItemException extends IllegalArgumentException {

    public InvalidOrderLineItemException() {
        super("주문 항목이 존재하지 않습니다.");
    }
}
