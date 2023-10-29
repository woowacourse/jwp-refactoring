package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTables;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.UngroupEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupEventListener {
    private final OrderRepository orderRepository;

    public TableUngroupEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void ungroup(final UngroupEvent event) {
        final OrderTables orderTables = event.getGroupedTables();
        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderRepository.findAllByOrderTableIds(orderTableIds)
                .forEach(Order::validatePossibleToUpdateOrderTable);
        orderTables.unbindTablesFromGroup();
    }
}
