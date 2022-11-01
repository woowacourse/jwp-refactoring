package kitchenpos.exception;

public class MenuGroupNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 메뉴 그룹입니다.";

    public MenuGroupNotFoundException() {
        super(MESSAGE);
    }
}
