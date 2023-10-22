package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.Empty;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.TableGroupRequest;
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
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroup create(final TableGroupRequest request) {
        final List<OrderTableIdDto> orderTableIdsDto = request.getOrderTables();
        final OrderTables orderTables = findOrderTables(orderTableIdsDto);
        validateRequestAndOrderTables(orderTableIdsDto, orderTables);
        orderTables.getOrderTables().forEach(this::validateOrderTable);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        saveOrderTable(orderTables, savedTableGroup);

        return savedTableGroup;
    }

    private void saveOrderTable(final OrderTables orderTables, final TableGroup savedTableGroup) {
        orderTables.updateAll(savedTableGroup.getId(), Empty.NOT_EMPTY);
        orderTables.getOrderTables().forEach(orderTableRepository::save);
    }

    private void validateOrderTable(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTables findOrderTables(final List<OrderTableIdDto> orderTableIdsDto) {
        final List<Long> orderTableIds = orderTableIdsDto.stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());

        return new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
    }

    private void validateRequestAndOrderTables(final List<OrderTableIdDto> orderTableIdsDto, final OrderTables orderTables) {
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
            orderTableRepository.save(orderTable);
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
