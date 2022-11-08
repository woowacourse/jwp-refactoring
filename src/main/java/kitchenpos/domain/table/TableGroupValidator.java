package kitchenpos.domain.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final OrderTableRepository orderTables;
    private final OrderRepository orders;

    public TableGroupValidator(final OrderTableRepository orderTables,
                               final OrderRepository orders) {
        this.orderTables = orderTables;
        this.orders = orders;
    }

    public void validateOnUngroup(final TableGroup tableGroup) {
        final var orderTableIds = collectOrderTableIds(tableGroup);
        final var ordersInOrderTables = orders.getOrderTableIdsIn(orderTableIds);

        final var allOrderCompleted = ordersInOrderTables.stream()
                .allMatch(order -> order.getOrderStatus() == OrderStatus.COMPLETION);
        if (!allOrderCompleted) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> collectOrderTableIds(final TableGroup tableGroup) {
        return tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
