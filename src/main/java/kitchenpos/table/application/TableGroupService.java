package kitchenpos.table.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.dto.TableGroupRequestDto;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequestDto tableGroupRequestDto) {

        final OrderTables savedOrderTables = validateInputOrderTable(tableGroupRequestDto.getOrderTableIds());
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), savedOrderTables.getOrderTables()));
        savedOrderTables.group(savedTableGroup.getId());
        for (OrderTable orderTable : savedOrderTables.getOrderTables()) {
            orderTableRepository.save(orderTable);
        }
        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    private OrderTables validateInputOrderTable(List<Long> orderTableIds) {

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTableSize(orderTableIds, savedOrderTables);
        validateOrderTable(savedOrderTables);

        return new OrderTables(savedOrderTables);
    }

    private void validateOrderTableSize(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.validateEmptyTable();
            savedOrderTable.validateNotGroupTable();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTableRepository.save(
                    new OrderTable(orderTable.getId(),
                            null,
                            orderTable.getNumberOfGuests(),
                            false)
            );
        }
    }
}
