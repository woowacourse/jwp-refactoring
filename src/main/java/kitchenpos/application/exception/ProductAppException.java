package kitchenpos.application.exception;

import kitchenpos.common.KitchenPosException;

public class ProductAppException extends KitchenPosException {

    private static final String NOT_FOUND_PRODUCT_MESSAGE = "상품을 찾을 수 없습니다. id = ";

    public ProductAppException(final String message) {
        super(message);
    }

    public static class NotFoundProductException extends ProductAppException {

        public NotFoundProductException(final Long productId) {
            super(NOT_FOUND_PRODUCT_MESSAGE + productId);
        }
    }
}
