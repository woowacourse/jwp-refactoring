package kitchenpos.exception.orderTableException;

public class InvalidOrderTableException extends OrderTableExcpetion {
    public static final String error = "잘못된 OrderTable 입니다.";
    public InvalidOrderTableException() {
        super(error);
    }
}
