package kitchenpos.tablegroup.exception;

public class TableGroupNotFoundException extends RuntimeException {

    public TableGroupNotFoundException() {
        super("일치하는 테이블 그룹이 없습니다.");
    }
}
