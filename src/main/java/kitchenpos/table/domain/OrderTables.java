package kitchenpos.table.domain;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void joinGroup(final TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            throw new IllegalArgumentException();
        }

        validate(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.registerGroup(tableGroup);
        }
    }

    private void validate(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.canBeGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void leaveGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.leaveGroup();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
