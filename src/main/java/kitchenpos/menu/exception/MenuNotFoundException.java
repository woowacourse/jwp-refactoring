package kitchenpos.menu.exception;

public class MenuNotFoundException extends RuntimeException {

    public MenuNotFoundException() {
        super("일치하는 메뉴가 없습니다.");
    }
}
