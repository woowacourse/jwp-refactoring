package kitchenpos.exception.tableGroupException;

import kitchenpos.exception.tableGroupException.TableGroupException;

public class TableGroupNotFoundException extends TableGroupException {
    private final static String error = "TableGroup을 찾을 수 없습니다.";
    public TableGroupNotFoundException() {
        super(error);
    }
}
