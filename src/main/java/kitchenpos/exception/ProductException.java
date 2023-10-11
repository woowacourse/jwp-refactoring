package kitchenpos.exception;

public class ProductException extends RuntimeException {

    public ProductException(final String message) {
        super(message);
    }

    public static class NotFoundProductException extends ProductException {
        public NotFoundProductException() {
            super("[ERROR] 해당하는 PRODUCT를 찾을 수 없습니다.");
        }
    }
}
