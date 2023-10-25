package kitchenpos.domain.exception;

import kitchenpos.common.KitchenPosException;

public class MenuProductException extends KitchenPosException {

    private static final String NO_MENU_PRODUCT_NAME_MESSAGE = "메뉴 상품 이름을 확인해주세요.";
    private static final String MINIMUM_PRICE_MESSAGE = "메뉴 상품 가격은 0미만이 될 수 없어요.";
    private static final String MINIMUM_QUANTITY_MESSAGE = "메뉴 상품 수량을 확인해주세요.";

    public MenuProductException(final String message) {
        super(message);
    }

    public static class NoMenuProductNameException extends MenuProductException {

        public NoMenuProductNameException() {
            super(NO_MENU_PRODUCT_NAME_MESSAGE);
        }
    }

    public static class MinimumPriceException extends MenuProductException {

        public MinimumPriceException() {
            super(MINIMUM_PRICE_MESSAGE);
        }
    }

    public static class MinimumQuantityException extends MenuProductException {

        public MinimumQuantityException() {
            super(MINIMUM_QUANTITY_MESSAGE);
        }
    }
}
