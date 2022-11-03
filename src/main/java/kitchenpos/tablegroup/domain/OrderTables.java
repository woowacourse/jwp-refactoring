package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void group(final Long tableGroupId, final int duplicateCheckSize) {
        validateSize(orderTables.size(), duplicateCheckSize);
        orderTables.forEach(orderTable -> orderTable.groupTable(tableGroupId));
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroupTable);
    }

    private void validateSize(final int orderTablesSize, final int duplicateCheckSize) {
        if (orderTablesSize != duplicateCheckSize) {
            throw new IllegalArgumentException("존재하지 않거나 중복된 테이블을 단체 지정할 수 없습니다.");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
