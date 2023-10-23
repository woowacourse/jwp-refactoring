package kitchenpos.domain.exception;

public abstract class OrderLineItemException extends KitchenPosException {

    public OrderLineItemException(String message) {
        super(message);
    }

    public static class InvalidMenuException extends OrderLineItemException {

        private static final String INVALID_MENU_MESSAGE = "메뉴는 반드시 1 이상이어야합니다.";

        public InvalidMenuException() {
            super(INVALID_MENU_MESSAGE);
        }
    }
}
