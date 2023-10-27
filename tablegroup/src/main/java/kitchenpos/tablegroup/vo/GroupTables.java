package kitchenpos.tablegroup.vo;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class GroupTables {

    private final List<OrderTable> orderTables;

    public GroupTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
