package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.CreateTableGroupRequest;
import kitchenpos.dto.request.OrderTableDto;
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
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        for (final OrderTable orderTable : savedOrderTables) {
            orderTable.group(tableGroup);
        }
        return tableGroup;
    }

    private List<OrderTable> findOrderTables(final CreateTableGroupRequest orderTableRequest) {
        final List<Long> orderTableIds = orderTableRequest.getOrderTables()
                                                          .stream()
                                                          .map(OrderTableDto::getId)
                                                          .collect(Collectors.toUnmodifiableList());
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdsIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size() || orderTableIds.size() < 2) {
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
