package kitchenpos.exception;

public class KitchenPosException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "처리에 실패했습니다. 잠시 후에 다시 요청해주세요";

    public KitchenPosException() {
        super(DEFAULT_MESSAGE);
    }

    public KitchenPosException(final String message) {
        super(message);
    }

    public KitchenPosException(final String message, final Exception e) {
        super(message, e);
    }
}
