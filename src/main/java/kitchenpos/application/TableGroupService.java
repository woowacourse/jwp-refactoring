package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.CreateTableGroupRequest;
import kitchenpos.ui.dto.OrderTableDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest orderTableRequests) {
        final List<OrderTable> savedOrderTables = findOrderTables(orderTableRequests);
        final TableGroup tableGroup = new TableGroup(savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.group(tableGroup);
            orderTableRepository.save(savedOrderTable);
        }

        return savedTableGroup;
    }

    private List<OrderTable> findOrderTables(final CreateTableGroupRequest orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.getOrderTables()
                                                           .stream()
                                                           .map(OrderTableDto::getId)
                                                           .collect(Collectors.toUnmodifiableList());
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdsIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderTableProgress(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroupById(tableGroupId);
            orderTableRepository.save(orderTable);
        }
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
