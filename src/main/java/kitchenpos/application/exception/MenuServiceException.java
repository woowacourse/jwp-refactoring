package kitchenpos.application.exception;

import java.math.BigDecimal;
import kitchenpos.domain.exception.KitchenPosException;

public class MenuServiceException extends KitchenPosException {

    public MenuServiceException(final String message) {
        super(message);
    }

    public static class NoPriceException extends MenuServiceException {

        private static final String NO_PRICE_MESSAGE = "금액은 0원 이상이어야 합니다. \n현재 금액: ";

        public NoPriceException(final BigDecimal price) {
            super(NO_PRICE_MESSAGE + price);
        }
    }

    public static class PriceMoreThanProductsException extends MenuServiceException {

        private static final String PRICE_MORE_THAN_PRODUCTS_MESSAGE = "금액은 상품의 총 금액보다 작거나 같아야합니다. \n메뉴 금액, 총 상품 금액: ";

        public PriceMoreThanProductsException(final BigDecimal menuPrice, final BigDecimal productsPrice) {
            super(PRICE_MORE_THAN_PRODUCTS_MESSAGE + menuPrice + ", " + productsPrice);
        }
    }

    public static class NotExistsMenuGroupException extends MenuServiceException {

        private static final String NOT_EXISTS_MENU_GROUP_MESSAGE = "전달받은 메뉴 그룹의 id가 없습니다. \nmenu gruop id: ";

        public NotExistsMenuGroupException(final Long menuGroupId) {
            super(NOT_EXISTS_MENU_GROUP_MESSAGE + menuGroupId);
        }
    }

    public static class NotExistsProductException extends MenuServiceException {

        private static final String NOT_EXISTS_PRODUCT_MESSAGE = "전달받은 상품 중 존재하지 않는 상품이 있습니다.";

        public NotExistsProductException() {
            super(NOT_EXISTS_PRODUCT_MESSAGE);
        }
    }
}
