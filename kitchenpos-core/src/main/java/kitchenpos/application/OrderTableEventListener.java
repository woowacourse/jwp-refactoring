package kitchenpos.application;

import kitchenpos.application.event.AddGroupTableEvent;
import kitchenpos.application.event.UngroupTableEvent;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderTableDto;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Component
public class OrderTableEventListener {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableEventListener(final OrderTableRepository orderRepository, final OrderRepository orderRepository1) {
        this.orderTableRepository = orderRepository;
        this.orderRepository = orderRepository1;
    }

    @EventListener
    public void handleCreateTableGroup(final AddGroupTableEvent event) {
        final List<OrderTableDto> orderTableDtos = event.getOrderTableDtos();
        final TableGroup tableGroup = event.getTableGroup();
        final List<OrderTable> savedOrderTables = findOrderTables(orderTableDtos);
        for (final OrderTable orderTable : savedOrderTables) {
            orderTable.group(tableGroup);
        }
    }

    private List<OrderTable> findOrderTables(final List<OrderTableDto> orderTableRequest) {
        final List<Long> orderTableIds = orderTableRequest.stream()
                                                          .map(OrderTableDto::getId)
                                                          .toList();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdsIn(orderTableIds);
        validateOrderTableSize(orderTableIds, orderTables);
        return orderTables;
    }

    private static void validateOrderTableSize(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size() || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @EventListener
    public void handleUngroupTable(final UngroupTableEvent ungroupTableEvent) {
        final Long tableGroupId = ungroupTableEvent.getTableGroupId();
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderTableProgress(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroupById(tableGroupId);
        }
        orderTableRepository.saveAll(orderTables);
    }

    private void validateOrderTableProgress(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new IllegalArgumentException();
        }
    }
}
