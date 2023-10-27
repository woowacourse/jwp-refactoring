package kitchenpos.table.application;

import java.util.List;

public interface TableGroupValidator {

    void validateUngroupable(List<Long> orderTableIds);
}
