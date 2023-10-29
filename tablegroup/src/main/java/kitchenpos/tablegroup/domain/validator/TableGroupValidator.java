package kitchenpos.tablegroup.domain.validator;

import java.util.List;

public interface TableGroupValidator {
    void validateOrderTable(final List<Long> orderTableIds);

    void validateCompletedOrderTableInTableGroup(final Long tableGroupId);
}
