package kitchenpos.menu.domain;

public class MenuException extends RuntimeException {

    public MenuException() {
        super("메뉴의 상태가 올바르지 않습니다.");
    }

    public MenuException(String message) {
        super(message);
    }
}
