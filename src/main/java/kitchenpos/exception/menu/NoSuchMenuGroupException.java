package kitchenpos.exception.menu;

public class NoSuchMenuGroupException extends RuntimeException {
    private static final String MESSAGE = "메뉴 그룹을 찾을 수 없습니다.";

    public NoSuchMenuGroupException() {
        super(MESSAGE);
    }
}
