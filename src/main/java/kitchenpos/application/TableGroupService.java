package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.tablegroup.OrderTableIdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroup create(final List<OrderTableIdRequest> orderTableIdRequests) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList()));

        validateOrderTableStatus(orderTables);

        return createTableGroupBy(orderTables);
    }

    private void validateOrderTableStatus(final List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException("단체 지정시 주문 테이블은 비어있을 수 없습니다.");
        }
    }

    private TableGroup createTableGroupBy(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.update(tableGroup, false);
            orderTableRepository.save(savedOrderTable);
        }

        tableGroup.updateOrderTables(orderTables);
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateOrderStatus(orderTableIds);

        unGroupOrderTable(orderTables);
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        orderRepository.findAllByOrderTableIdIn(orderTableIds)
                .forEach(Order::validateUncompleted);
    }

    private void unGroupOrderTable(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
            orderTableRepository.save(orderTable);
        }
    }
}
