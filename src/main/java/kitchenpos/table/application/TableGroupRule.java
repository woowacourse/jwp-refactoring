package kitchenpos.table.application;

import kitchenpos.table.domain.collection.OrderTables;

public interface TableGroupRule {

    boolean unableToUngroup(OrderTables orderTables);
}
