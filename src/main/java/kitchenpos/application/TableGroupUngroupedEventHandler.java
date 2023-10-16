package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroupUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableGroupUngroupedEventHandler {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupUngroupedEventHandler(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener(TableGroupUngroupedEvent.class)
    public void handle(TableGroupUngroupedEvent event) {
        List<Long> tableIds = orderTableRepository.findAllByTableGroupId(event.getTableGroupId()).stream()
                .map(OrderTable::getId)
                .collect(toList());
        
        orderRepository.findAllByOrderTableIds(tableIds)
                .forEach(Order::validateUngroupTableAllowed);
    }
}
