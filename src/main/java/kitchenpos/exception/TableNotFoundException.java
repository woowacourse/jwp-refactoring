package kitchenpos.exception;

public class TableNotFoundException extends RuntimeException {

    public TableNotFoundException() {
        super("테이블을 찾을 수 없습니다. 누락 가능성이 있습니다.");
    }
}
