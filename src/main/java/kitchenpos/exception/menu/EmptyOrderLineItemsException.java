package kitchenpos.exception.menu;

public class EmptyOrderLineItemsException extends RuntimeException {
    private static final String MESSAGE = "주문할 메뉴가 없습니다.";

    public EmptyOrderLineItemsException() {
        super(MESSAGE);
    }
}
