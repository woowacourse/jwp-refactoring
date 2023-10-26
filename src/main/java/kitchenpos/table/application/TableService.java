package kitchenpos.table.application;

import kitchenpos.common.GroupOrderTablesEvent;
import kitchenpos.common.UngroupOrderTableEvent;
import kitchenpos.common.ValidateOrderTableOrderStatusEvent;
import kitchenpos.common.ValidateOrderTablesOrderStatusEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.InvalidOrderTableException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.DuplicateOrderTableException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableService(final OrderTableRepository orderTableRepository, final ApplicationEventPublisher eventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(null, orderTableRequest.getNumberOfGuest(), orderTableRequest.getEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);

        eventPublisher.publishEvent(new ValidateOrderTableOrderStatusEvent(orderTable.getId()));
        orderTable.changeEmptyStatus(orderTableRequest.getEmpty());

        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        final OrderTable newOrderTable = new OrderTable(orderTable.getTableGroupId(), orderTableRequest.getNumberOfGuest(), orderTableRequest.getEmpty());

        final OrderTable savedOrder = orderTableRepository.save(newOrderTable);

        return OrderTableResponse.from(savedOrder);
    }

    @EventListener
    public void ungroup(final UngroupOrderTableEvent ungroupOrderTableEvent) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(ungroupOrderTableEvent.getTableGroupId());
        final List<Long> orderTableIds = orderTables.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        eventPublisher.publishEvent(new ValidateOrderTablesOrderStatusEvent(orderTableIds));

        orderTables.stream().forEach(orderTable -> orderTable.ungroup());
    }

    @EventListener
    public void group(final GroupOrderTablesEvent groupOrderTablesEvent) {
        final List<Long> orderTableIds = groupOrderTablesEvent.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findByIdIn(orderTableIds);

        validateOrderTable(orderTables, orderTableIds);

        orderTables.stream().forEach(orderTable -> orderTable.changeTableGroup(groupOrderTablesEvent.getTableGroupId()));
    }

    private static void validateOrderTable(final List<OrderTable> orderTables, final List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new InvalidOrderTableException();
        }

        for (final OrderTable savedOrderTable : orderTables) {
            if (savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new DuplicateOrderTableException();
            }
        }
    }
}
