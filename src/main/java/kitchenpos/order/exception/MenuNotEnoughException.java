package kitchenpos.order.exception;

public class MenuNotEnoughException extends IllegalArgumentException {

    private static final String MESSAGE = "메뉴 없이 주문할 수 없습니다.";

    public MenuNotEnoughException() {
        super(MESSAGE);
    }
}
