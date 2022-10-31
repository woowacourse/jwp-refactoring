package kitchenpos.exceptions;

public class OrderLineItemsEmptyException extends RuntimeException {

    public OrderLineItemsEmptyException() {
        super("주문항목이 존재하지 않습니다.");
    }
}
