package kitchenpos.exception;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException() {
        super("메뉴가 서버에 저장되어 있지 않습니다.");
    }
}
