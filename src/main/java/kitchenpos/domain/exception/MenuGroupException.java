package kitchenpos.domain.exception;

public class MenuGroupException extends KitchenPosException{

    public MenuGroupException(String message) {
        super(message);
    }

    public static class NotExistsMenuGroupException extends MenuGroupException {

        private static final String NOT_EXISTS_MENU_GROUP_MESSAGE = "메뉴 그룹이 없습니다. 현재 메뉴 그룹: ";

        public NotExistsMenuGroupException(final Long menuGroupId) {
            super(NOT_EXISTS_MENU_GROUP_MESSAGE + menuGroupId);
        }
    }
}
