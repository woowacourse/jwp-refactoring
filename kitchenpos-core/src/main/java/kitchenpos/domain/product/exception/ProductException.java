package kitchenpos.domain.product.exception;


import kitchenpos.exception.CustomException;

public class ProductException extends CustomException {

    public ProductException(final String message) {
        super(message);
    }

    public static class NotFoundProductException extends ProductException {
        public NotFoundProductException() {
            super("해당하는 PRODUCT를 찾을 수 없습니다.");
        }
    }
}
