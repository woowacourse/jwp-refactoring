package kitchenpos.table.common;

import kitchenpos.common.UngroupOrderTableEvent;
import kitchenpos.common.ValidateOrderTablesOrderStatusEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UnGroupOrderTableEventListener {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UnGroupOrderTableEventListener(final OrderTableRepository orderTableRepository, final ApplicationEventPublisher eventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void ungroup(final UngroupOrderTableEvent ungroupOrderTableEvent) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(ungroupOrderTableEvent.getTableGroupId());
        final List<Long> orderTableIds = orderTables.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        eventPublisher.publishEvent(new ValidateOrderTablesOrderStatusEvent(orderTableIds));

        orderTables.stream()
                .forEach(orderTable -> orderTable.ungroup());
    }
}
