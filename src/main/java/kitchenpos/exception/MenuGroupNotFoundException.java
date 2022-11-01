package kitchenpos.exception;

public class MenuGroupNotFoundException extends RuntimeException {

    public MenuGroupNotFoundException() {
        super();
    }

    public MenuGroupNotFoundException(final String message) {
        super(message);
    }
}
