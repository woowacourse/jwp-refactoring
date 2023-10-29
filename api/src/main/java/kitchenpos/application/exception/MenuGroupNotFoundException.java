package kitchenpos.application.exception;

public class MenuGroupNotFoundException extends IllegalArgumentException {

    public MenuGroupNotFoundException() {
        super("지장한 메뉴 그룹을 찾을 수 없습니다.");
    }
}
