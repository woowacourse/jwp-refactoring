package kitchenpos.exception;

public class MenuNotExistException extends RuntimeException {
    public MenuNotExistException() {
        super("메뉴가 존재하지 않습니다");
    }
}
