package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableUngroupEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableUngroupEventHandler(
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void handle(final TableUngroupEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getUngroupTableId());
        validate(orderTables);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validate(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateUngroupAvailable(orderTable.getId());
        }
    }

    private void validateUngroupAvailable(final Long orderTableId) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (validateAllOrdersCompleted(orders)) {
            return;
        }
        throw new IllegalArgumentException("Cannot ungroup non-completed table.");
    }

    private boolean validateAllOrdersCompleted(final List<Order> orders) {
        return orders.stream().allMatch(Order::isCompleted);
    }
}
