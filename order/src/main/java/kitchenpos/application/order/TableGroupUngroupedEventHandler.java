package kitchenpos.application.order;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroupUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableGroupUngroupedEventHandler {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupUngroupedEventHandler(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener(TableGroupUngroupedEvent.class)
    public void handle(TableGroupUngroupedEvent event) {
        List<Long> tableIds = getTableIds(event.getTableGroupId());
        orderRepository.findAllByOrderTableIds(tableIds)
                .forEach(Order::validateUngroupTableAllowed);
    }

    private List<Long> getTableIds(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId).stream()
                .map(OrderTable::getId)
                .collect(toList());
    }
}
