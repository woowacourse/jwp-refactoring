package kitchenpos.domain.table;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.canBeGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void joinGroup(final Long tableGroupId) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.registerGroup(tableGroupId);
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
