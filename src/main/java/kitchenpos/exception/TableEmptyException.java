package kitchenpos.exception;

public class TableEmptyException extends IllegalArgumentException {

    public TableEmptyException() {
        super("테이블이 비어있어서 주문을 할 수 없습니다");
    }
}
