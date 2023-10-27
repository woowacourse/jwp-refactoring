package kitchenpos.table.exception;

public class DuplicateOrderTableException extends OrderTableExcpetion {
    public static final String error = "이미 지정된 테이블은 단체 지정할 수 없습니다.";

    public DuplicateOrderTableException() {
        super(error);
    }
}
