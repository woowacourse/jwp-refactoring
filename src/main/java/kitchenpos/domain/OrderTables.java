package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class OrderTables {
    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void changeStatus() {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeStatus(false);
        }
    }

    public List<Long> extractIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeStatus(false);
            orderTable.ungroup();
        }
    }
}
