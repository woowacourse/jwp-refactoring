package kitchenpos.domain.table;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                          .map(OrderTable::getId)
                          .collect(Collectors.toList());
    }

    public void ungroupOrderTables() {
        if (containsDifferentId()) {
            throw new IllegalArgumentException("다른 테이블 그룹에 속한 테이블이 포함되어 있습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private boolean containsDifferentId() {
        final long orderTableIdCount = orderTables.stream()
                                                  .map(OrderTable::getTableGroupId)
                                                  .distinct()
                                                  .count();

        return orderTableIdCount != 1;
    }
}
