package kitchenpos.exception.table;

public class NoSuchOrderTableException extends RuntimeException {
    private static final String MESSAGE = "테이블을 찾을 수 없습니다.";

    public NoSuchOrderTableException() {
        super(MESSAGE);
    }
}
