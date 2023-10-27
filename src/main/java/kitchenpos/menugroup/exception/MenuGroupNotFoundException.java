package kitchenpos.menugroup.exception;

public class MenuGroupNotFoundException extends RuntimeException {

    private static final String MESSAGE = "메뉴 그룹을 찾을 수 없습니다.";

    public MenuGroupNotFoundException() {
        super(MESSAGE);
    }
}
