package kitchenpos.application.exception;

import kitchenpos.common.KitchenPosException;

public class MenuAppException extends KitchenPosException {

    private static final String NOT_FOUND_MENU_EXCEPTION = "메뉴를 찾을 수 없습니다. id = ";

    public MenuAppException(final String message) {
        super(message);
    }

    public static class NotFoundMenuException extends MenuAppException {

        public NotFoundMenuException(final Long menuId) {
            super(NOT_FOUND_MENU_EXCEPTION + menuId);
        }
    }
}
