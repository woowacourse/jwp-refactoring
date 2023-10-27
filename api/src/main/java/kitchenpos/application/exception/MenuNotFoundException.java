package kitchenpos.application.exception;

public class MenuNotFoundException extends IllegalArgumentException {

    public MenuNotFoundException() {
        super("지정한 메뉴를 찾을 수 없습니다.");
    }
}
