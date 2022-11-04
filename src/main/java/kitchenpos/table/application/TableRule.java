package kitchenpos.table.application;

import kitchenpos.table.domain.entity.OrderTable;

public interface TableRule {

    boolean unableToChangeEmpty(OrderTable orderTable);
}
