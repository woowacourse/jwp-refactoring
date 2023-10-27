package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.UnGroupEvent;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableUnGroupEventListener {
    private final OrderRepository orderRepository;

    public TableUnGroupEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void unGroup(final UnGroupEvent event) {
        final OrderTables orderTables = event.getGroupedTables();
        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderRepository.findAllByOrderTableIds(orderTableIds)
                .forEach(Order::validatePossibleToUpdateOrderTable);
        orderTables.unbindTablesFromGroup();
    }
}
