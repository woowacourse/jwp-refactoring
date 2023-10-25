package kitchenpos.exception.tableGroupException;

public class DuplicateCreateTableGroupException extends TableGroupException {
    public static final String error = "이미 지정된 테이블은 단체 지정할 수 없습니다.";

    public DuplicateCreateTableGroupException() {
        super(error);
    }
}
