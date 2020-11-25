package kitchenpos.domain.table;

import kitchenpos.domain.order.OrderTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = new ArrayList<>(orderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables, int size) {
        if (size != orderTables.size()) {
            throw new IllegalArgumentException("요청한 Table 중 DB에 저장되어있지 않은 Table이 있습니다.");
        }
        return new OrderTables(orderTables);
    }

    public List<OrderTable> group(TableGroup tableGroup) {
        return orderTables.stream()
                .map(orderTable -> orderTable.group(tableGroup))
                .collect(Collectors.toList());
    }

    public List<OrderTable> ungroup() {
        return orderTables.stream()
                .map(orderTable -> orderTable.unGroup())
                .collect(Collectors.toList());
    }

    public List<Long> extractIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}

