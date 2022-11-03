package kitchenpos.exception;

public class OrderLineItemSizeException extends IllegalArgumentException {

    public OrderLineItemSizeException() {
        super("주문 항목의 사이즈가 올바르지 않습니다");
    }
}
