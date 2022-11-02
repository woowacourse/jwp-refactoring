package kitchenpos.exception;

public class MenuGroupNotFoundException extends RuntimeException {
    public MenuGroupNotFoundException() {
        super("서버에 일치하는 메뉴 그룹을 찾지 못했습니다.");
    }
}
