package kitchenpos.exception;

public class EmptyMenuOrderException extends BusinessException {
    public EmptyMenuOrderException() {
        super("Order must has 1 menu at least.");
    }
}
