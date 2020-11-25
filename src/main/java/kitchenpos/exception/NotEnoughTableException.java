package kitchenpos.exception;

public class NotEnoughTableException extends RuntimeException {
    public NotEnoughTableException() {
        super("테이블 갯수가 충분하지 않습니다");
    }
}
