package kitchenpos.menu.domain.exception;

import java.math.BigDecimal;
import kitchenpos.KitchenPosException;

public abstract class MenuException extends KitchenPosException {

    public MenuException(final String message) {
        super(message);
    }

    public static class PriceMoreThanProductsException extends MenuException {

        private static final String PRICE_MORE_THAN_PRODUCTS_MESSAGE = "금액은 상품의 총 금액보다 작거나 같아야합니다. \n메뉴 금액, 총 상품 금액: ";

        public PriceMoreThanProductsException(final BigDecimal menuPrice, final BigDecimal productsPrice) {
            super(PRICE_MORE_THAN_PRODUCTS_MESSAGE + menuPrice + ", " + productsPrice);
        }
    }

    public static class NotExistsProductException extends MenuException {

        private static final String NOT_EXISTS_PRODUCT_MESSAGE = "상품에 존재하지 않는 상품이 있습니다.";

        public NotExistsProductException() {
            super(NOT_EXISTS_PRODUCT_MESSAGE);
        }
    }

    public static class InvalidMenuNameException extends MenuException {

        private static final String INVALID_MENU_NAME_MESSAGE = "메뉴 이름은 반드시 존재해야합니다.";

        public InvalidMenuNameException() {
            super(INVALID_MENU_NAME_MESSAGE);
        }
    }
}
