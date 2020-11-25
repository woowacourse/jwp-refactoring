package kitchenpos.exception;

public class MenuGroupNotExistException extends RuntimeException {
    public MenuGroupNotExistException() {
        super("메뉴 그룹이 존재하지 않습니다");
    }
}
