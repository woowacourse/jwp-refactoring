package kitchenpos.menugroup.domain.exception;

import kitchenpos.KitchenPosException;

public abstract class MenuGroupException extends KitchenPosException {

    public MenuGroupException(final String message) {
        super(message);
    }

    public static class NotExistsMenuGroupException extends MenuGroupException {

        private static final String NOT_EXISTS_MENU_GROUP_MESSAGE = "메뉴 그룹이 없습니다. 현재 메뉴 그룹: ";

        public NotExistsMenuGroupException(final Long menuGroupId) {
            super(NOT_EXISTS_MENU_GROUP_MESSAGE + menuGroupId);
        }
    }

    public static class InvalidMenuGroupNameException extends MenuGroupException {

        private static final String INVALID_MENU_GROUP_NAME_MESSAGE = "메뉴 그룹 이름은 반드시 존재해야합니다.";

        public InvalidMenuGroupNameException() {
            super(INVALID_MENU_GROUP_NAME_MESSAGE);
        }
    }
}
