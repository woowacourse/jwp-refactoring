package kitchenpos.tablegroup.vo;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;

public class GroupTables {

    private final List<OrderTable> orderTables;

    public GroupTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
}
