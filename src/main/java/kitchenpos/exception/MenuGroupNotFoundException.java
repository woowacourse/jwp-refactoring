package kitchenpos.exception;

public class MenuGroupNotFoundException extends RuntimeException {
    public static final String error = "MenuGroup을 찾을 수 없습니다.";
    public MenuGroupNotFoundException() {
        super(error);
    }
}
