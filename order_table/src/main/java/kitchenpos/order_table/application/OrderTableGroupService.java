package kitchenpos.order_table.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.domain.OrderTableGroup;
import kitchenpos.order_table.domain.OrderTables;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order_table.domain.repository.OrderTableRepository;
import kitchenpos.order_table.domain.repository.TableGroupRepository;
import kitchenpos.order_table.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderTableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public OrderTableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
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

        OrderTableGroup orderTableGroup = new OrderTableGroup(orderTables, LocalDateTime.now());
        orderTables.updateTableGroupIds(orderTableGroup);
        return tableGroupRepository.save(orderTableGroup).getId();
    }

    private void validateSavedOrderIsEmpty(List<OrderTable> savedOrderTables) {
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
