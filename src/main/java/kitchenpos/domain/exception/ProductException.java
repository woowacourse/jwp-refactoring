package kitchenpos.domain.exception;

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
}
