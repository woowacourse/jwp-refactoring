package kitchenpos.application.event;

import kitchenpos.domain.validator.OrderTableValidator;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableGroupEventListener {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupEventListener(final OrderTableRepository orderTableRepository,
                                   final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @EventListener
    @Transactional
    public void handleTableGroupEvent(final TableGroupEvent tableGroupEvent) {
        final var tableGroup = tableGroupEvent.getTableGroup();
        final var tableGroupId = tableGroup.getId();
        final var orderTableIds = tableGroup.getOrderTableIds();

        orderTableRepository.findAllByIdInAndEmptyIsTrueAndTableGroupIdIsNull(orderTableIds)
                .forEach(table -> table.group(orderTableValidator, tableGroupId));
    }

    @EventListener
    @Transactional
    public void handleTableUnGroupEvent(final TableUnGroupEvent tableUnGroupEvent) {
        final var tableGroupId = tableUnGroupEvent.getTableGroupId();

        orderTableRepository.findAllByTableGroupId(tableGroupId)
                .forEach(table -> table.unGroup(orderTableValidator));
    }
}
