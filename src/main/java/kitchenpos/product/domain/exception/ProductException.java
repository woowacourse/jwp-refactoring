package kitchenpos.product.domain.exception;

import kitchenpos.KitchenPosException;

public abstract class ProductException extends KitchenPosException {

    public ProductException(String message) {
        super(message);
    }

    public static class InvalidProductNameException extends ProductException {

        private static final String INVALID_PRODUCT_NAME_MESSAGE = "상품 이름은 반드시 존재해야합니다.";

        public InvalidProductNameException() {
            super(INVALID_PRODUCT_NAME_MESSAGE);
        }
    }

    public static class NotExistsProductException extends ProductException {

        private static final String NOT_EXISTS_PRODUCT_MESSAGE = "상품이 존재하지 않습니다. 상품 번호: ";

        public NotExistsProductException(final Long id) {
            super(NOT_EXISTS_PRODUCT_MESSAGE + id);
        }
    }
}
