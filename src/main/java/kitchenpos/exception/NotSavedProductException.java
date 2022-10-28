package kitchenpos.exception;

public class NotSavedProductException extends RuntimeException {
    public NotSavedProductException() {
        super("저장되지 않은 제품을 사용할 수 없습니다.");
    }
}
