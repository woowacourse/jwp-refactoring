package kitchenpos;

public abstract class KitchenPosException extends RuntimeException {

    public KitchenPosException(final String message) {
        super(message);
    }
}
