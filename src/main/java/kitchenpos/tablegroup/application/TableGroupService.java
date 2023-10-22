package kitchenpos.tablegroup.application;

import kitchenpos.order.Empty;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.OrderTable;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.application.OrderTableRepository;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.application.request.OrderTableIdDto;
import kitchenpos.tablegroup.application.request.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository,
                             final OrderRepository orderRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroup create(final TableGroupRequest request) {
        final List<OrderTableIdDto> orderTableIdsDto = request.getOrderTables();
        final List<OrderTable> orderTables = findOrderTables(orderTableIdsDto);
        validateRequestAndOrderTables(orderTableIdsDto, orderTables);
        orderTables.forEach(this::validateOrderTable);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        saveOrderTable(orderTables, savedTableGroup);

        return savedTableGroup;
    }

    private void saveOrderTable(final List<OrderTable> orderTables, final TableGroup savedTableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(savedTableGroup.getId());
            orderTable.updateEmpty(Empty.NOT_EMPTY);
        }
    }

    private void validateOrderTable(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> findOrderTables(final List<OrderTableIdDto> orderTableIdsDto) {
        final List<Long> orderTableIds = orderTableIdsDto.stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private void validateRequestAndOrderTables(final List<OrderTableIdDto> orderTableIdsDto, final List<OrderTable> orderTables) {
        if (orderTableIdsDto.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateExistenceByOrderTableIdsAndOrderStatuses(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(null);
            orderTable.updateEmpty(Empty.NOT_EMPTY);
        }
    }

    private void validateExistenceByOrderTableIdsAndOrderStatuses(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
