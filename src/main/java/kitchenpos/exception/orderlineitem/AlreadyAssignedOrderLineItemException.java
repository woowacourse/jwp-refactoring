package kitchenpos.exception.orderlineitem;

public class AlreadyAssignedOrderLineItemException extends RuntimeException {
    private static final String MESSAGE = "이미 등록된 주문 메뉴 입니다.";

    public AlreadyAssignedOrderLineItemException() {
        super(MESSAGE);
    }
}
