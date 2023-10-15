package kitchenpos.exception;

public class MenuGroupNotFoundException extends RuntimeException {

    public MenuGroupNotFoundException() {
        super("메뉴 그룹을 찾을 수 없습니다");
    }
}
