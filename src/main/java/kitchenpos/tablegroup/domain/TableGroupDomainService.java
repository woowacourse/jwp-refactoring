package kitchenpos.tablegroup.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.DomainService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.util.CollectionUtils;

@DomainService
public class TableGroupDomainService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupDomainService(final OrderRepository orderRepository,
                                   final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void addOrderTable(final TableGroup tableGroup, final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        validateOrderTableSizes(orderTables, orderTableIds);
        for (OrderTable orderTable : orderTables) {
            orderTable.addTableGroup(tableGroup.getId());
        }
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = getOrderTableIds(orderTables);
        validateOrderStatus(orderTableIds);
        ungroupOrderTables(orderTables);
    }

    private void validateOrderTableSizes(final List<OrderTable> orderTables, final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("OrderTable의 크기가 2 미만입니다.");
        }

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("입력받은 OrderTable 중 존재하지 않는 것이 있습니다.");
        }
    }

    private List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("해당 TableGroup의 Order 중 완료되지 않은 것이 존재합니다.");
        }
    }

    private void ungroupOrderTables(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.deleteTableGroup();
        }
    }
}
