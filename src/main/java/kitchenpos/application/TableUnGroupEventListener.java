package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.GroupedTables;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.UnGroupEvent;
import kitchenpos.domain.repository.OrderRepository;
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
        final GroupedTables groupedTables = event.getGroupedTables();
        final List<Long> orderTableIds = groupedTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderRepository.findAllByOrderTableIds(orderTableIds)
                .forEach(Order::validatePossibleToUpdateOrderTable);
        groupedTables.unbindTablesFromGroup();
    }
}
