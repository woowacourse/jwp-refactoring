package kitchenpos.domain.exception;

import java.math.BigDecimal;

public abstract class MenuException extends KitchenPosException {

    public MenuException(String message) {
        super(message);
    }

    public static class PriceMoreThanProductsException extends MenuException {

        private static final String PRICE_MORE_THAN_PRODUCTS_MESSAGE = "금액은 상품의 총 금액보다 작거나 같아야합니다. \n메뉴 금액, 총 상품 금액: ";

        public PriceMoreThanProductsException(final BigDecimal menuPrice, final BigDecimal productsPrice) {
            super(PRICE_MORE_THAN_PRODUCTS_MESSAGE + menuPrice + ", " + productsPrice);
        }
    }
}
