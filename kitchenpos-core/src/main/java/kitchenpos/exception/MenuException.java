package kitchenpos.exception;

import kitchenpos.common.KitchenPosException;

public class MenuException extends KitchenPosException {

    private static final String MENU_OVER_PRICE_EXCEPTION = "메뉴 가격은 상품 가격들 보다 높을 수 없습니다.";

    public MenuException(final String message) {
        super(message);
    }

    public static class MenuOverPriceException extends MenuException {

        public MenuOverPriceException() {
            super(MENU_OVER_PRICE_EXCEPTION);
        }
    }
}
