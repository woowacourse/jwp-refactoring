package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<Long> orderTableIds = tableGroupCreateRequest.getOrderTableIds();
        validateOrderTableIds(orderTableIds);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateSavedOrderTable(orderTableIds, savedOrderTables);

        final TableGroup savedTableGroup
            = saveTableGroup(tableGroupCreateRequest, savedOrderTables);

        saveNewOrderTable(savedOrderTables, savedTableGroup);

        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    private void validateOrderTableIds(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

    }

    private void validateSavedOrderTable(List<Long> orderTableIds,
        List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private TableGroup saveTableGroup(
        TableGroupCreateRequest tableGroupCreateRequest,
        List<OrderTable> savedOrderTables) {
        TableGroup tableGroup
            = tableGroupCreateRequest.toEntity(LocalDateTime.now(), savedOrderTables);
        return tableGroupRepository.save(tableGroup);
    }

    private void saveNewOrderTable(List<OrderTable> savedOrderTables,
        TableGroup savedTableGroup) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            OrderTable newOrderTable = new OrderTable(savedOrderTable.getId(), savedTableGroup,
                savedOrderTable.getNumberOfGuests(), false);

            orderTableRepository.save(newOrderTable);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository
            .findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        saveChangedOrderTable(orderTables);
    }

    private void saveChangedOrderTable(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            OrderTable changedOrderTable = new OrderTable(orderTable.getId(), null,
                orderTable.getNumberOfGuests(), false);

            orderTableRepository.save(changedOrderTable);
        }
    }
}
