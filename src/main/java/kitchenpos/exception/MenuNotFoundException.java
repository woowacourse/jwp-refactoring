package kitchenpos.exception;

public class MenuNotFoundException extends RuntimeException {
    public static final String error = "메뉴를 찾을 수 없습니다.";
    public MenuNotFoundException() {
        super(error);
    }
}
