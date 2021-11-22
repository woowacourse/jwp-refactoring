package kitchenpos.exception;

public class NotFoundMenuGroupException extends KitchenPosException {

    private static final String NOT_FOUND_MENU_GROUP = "메뉴 그룹이 존재하지 않습니다.";
    public NotFoundMenuGroupException() {
        super(NOT_FOUND_MENU_GROUP);
    }
}
