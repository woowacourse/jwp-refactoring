package kitchenpos.exception;

public class NotSavedMenuException extends RuntimeException {
    public NotSavedMenuException() {
        super("저장되지 않은 메뉴로 메뉴 제품을 만들 수 없습니다.");
    }
}
