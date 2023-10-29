package kitchenpos.application.exception;

public class TableGroupNotFoundException extends IllegalArgumentException {

    public TableGroupNotFoundException() {
        super("테이블을 단체 지정한 내역을 찾을 수 없습니다.");
    }
}
