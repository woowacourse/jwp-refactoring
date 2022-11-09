package kitchenpos.exception;

public class NotFoundMenuException extends IllegalArgumentException {

    public NotFoundMenuException() {
        super("메뉴를 찾을 수 없습니다.");
    }
}
