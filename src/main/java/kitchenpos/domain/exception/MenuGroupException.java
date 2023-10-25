package kitchenpos.domain.exception;

import static kitchenpos.domain.menu.MenuGroup.MINIMUM_MENU_GROUP_NAME_LENGTH;

import kitchenpos.common.KitchenPosException;

public class MenuGroupException extends KitchenPosException {

    private static final String NO_MENU_GROUP_NAME_MESSAGE =
        "메뉴그룹명은 최소 " + MINIMUM_MENU_GROUP_NAME_LENGTH + "자 이상이어야 합니다.";

    public MenuGroupException(final String message) {
        super(message);
    }

    public static class NoMenuGroupNameException extends MenuGroupException {

        public NoMenuGroupNameException() {
            super(NO_MENU_GROUP_NAME_MESSAGE);
        }
    }
}
