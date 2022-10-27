package kitchenpos.exception;

public class OrderLineItemEmptyException extends IllegalArgumentException {

    public OrderLineItemEmptyException() {
        super("주문 항목이 비어있습니다");
    }
}
