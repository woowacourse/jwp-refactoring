package kitchenpos.exception.product;

public class NoSuchProductException extends RuntimeException {
    private static final String MESSAGE = "상품을 찾을 수 없습니다.";

    public NoSuchProductException() {
        super(MESSAGE);
    }
}
