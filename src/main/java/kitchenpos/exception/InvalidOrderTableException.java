package kitchenpos.exception;

public class InvalidOrderTableException extends RuntimeException {
    public static final String error = "잘못된 OrderTable 입니다.";
    public InvalidOrderTableException() {
        super(error);
    }
}
