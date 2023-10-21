package kitchenpos.domain.exception;

public abstract class KitchenPosException extends RuntimeException {

    public KitchenPosException(final String message) {
        super(message);
    }
}
