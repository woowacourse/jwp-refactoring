package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public interface TableValidator {
    void validateChangeStatus(OrderTable orderTable);

    void validateUngroup(TableGroup tableGroup);
}
