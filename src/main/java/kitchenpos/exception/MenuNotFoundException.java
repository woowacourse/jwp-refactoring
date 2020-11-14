package kitchenpos.exception;

public class MenuNotFoundException extends BusinessException {
    public MenuNotFoundException() {
        super("Menu is not exist.");
    }
}
