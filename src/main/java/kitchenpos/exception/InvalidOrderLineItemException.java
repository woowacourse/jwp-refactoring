package kitchenpos.exception;

public class InvalidOrderLineItemException extends RuntimeException {
    private final static String error = "주문의 OrderLineItem은 비어있을 수 없습니다.";
    public InvalidOrderLineItemException() {
        super(error);
    }
}
