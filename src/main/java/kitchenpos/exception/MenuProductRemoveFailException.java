package kitchenpos.exception;

public class MenuProductRemoveFailException extends RuntimeException {
    public MenuProductRemoveFailException() {
        super("메뉴에서 메뉴 항목을 제거하지 못했습니다.");
    }
}
