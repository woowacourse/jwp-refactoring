package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public Long create(final TableGroupCreateRequest request) {
        OrderTables orderTables = new OrderTables(
                request.getOrderTableIds().stream()
                        .map(id -> orderTableRepository.getById(id))
                        .collect(Collectors.toList())
        );
        orderTables.validateOrderTablesSize();

        final List<Long> orderTableIds = orderTables.getValuesId();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        orderTables.validateOrderTablesSizeEqualToSavedOrderTablesSize(savedOrderTables);
        validateSavedOrderIsEmpty(savedOrderTables);

        TableGroup tableGroup = new TableGroup(orderTables, LocalDateTime.now());
        return tableGroupRepository.save(tableGroup).getId();
    }

    private static void validateSavedOrderIsEmpty(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = tableGroupRepository.getById(tableGroupId).getOrderTables();
        final List<Long> orderTableIds = orderTables.getValuesId();
        validateOrderTableStatus(orderTableIds);
        orderTables.ungroupOrderTables();
    }

    private void validateOrderTableStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

}
