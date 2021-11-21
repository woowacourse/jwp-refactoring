package kitchenpos.exception;

public class NotFoundMenuException extends KitchenPosException {

    private static final String NOT_FOUND_MENU = "존재하지 않는 메뉴입니다.";

    public NotFoundMenuException() {
        super(NOT_FOUND_MENU);
    }
}
