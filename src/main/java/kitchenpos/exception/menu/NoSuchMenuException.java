package kitchenpos.exception.menu;

public class NoSuchMenuException extends RuntimeException {
    private static final String MESSAGE = "메뉴를 찾을 수 없습니다.";

    public NoSuchMenuException() {
        super(MESSAGE);
    }
}
