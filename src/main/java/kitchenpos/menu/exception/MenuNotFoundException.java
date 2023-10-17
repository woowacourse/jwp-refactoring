package kitchenpos.menu.exception;

public class MenuNotFoundException extends RuntimeException {

    private static final String MESSAGE = "메뉴를 찾을 수 없습니다.";

    public MenuNotFoundException() {
        super(MESSAGE);
    }
}
