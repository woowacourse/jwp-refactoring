package kitchenpos.domain.exception;

public class InvalidMenuException extends RuntimeException {

    public InvalidMenuException() {
        super("메뉴의 상태가 올바르지 않습니다.");
    }

    public InvalidMenuException(String message) {
        super(message);
    }
}
