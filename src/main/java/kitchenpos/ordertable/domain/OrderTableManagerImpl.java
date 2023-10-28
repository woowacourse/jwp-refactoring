package kitchenpos.ordertable.domain;

import kitchenpos.exception.InvalidOrderTableToTableGroup;
import kitchenpos.exception.InvalidOrderTablesSize;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.OrderTableManager;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderTableManagerImpl implements OrderTableManager {

    private OrderTableRepository orderTableRepository;
    private OrderManager orderManager;

    public OrderTableManagerImpl(final OrderTableRepository orderTableRepository, final OrderManager orderManager) {
        this.orderTableRepository = orderTableRepository;
        this.orderManager = orderManager;
    }

    @Override
    public void addOrderTables(final TableGroup tableGroup, final List<Long> OrderTableIds) {
        final List<OrderTable> orderTables = findAllOrderTables(OrderTableIds);
        validateOrderTables(orderTables);

        addOrderTables(orderTables, tableGroup);
    }

    private List<OrderTable> findAllOrderTables(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTables(orderTableIds, orderTables);

        return orderTables;
    }

    private void validateOrderTables(final List<Long> orderTableDtos, final List<OrderTable> orderTables) {
        if (orderTables.size() != orderTableDtos.size()) {
            throw new NotFoundOrderTableException("존재하지 않는 주문 테이블이 있습니다.");
        }
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderTablesSize("주문 테이블은 2개 이상 있어야 합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            validateOrderTableHasTableGroup(orderTable);
        }
    }

    private void validateOrderTableHasTableGroup(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new InvalidOrderTableToTableGroup("주문 테이블이 테이블 그룹을 만들 수 없는 상태입니다.");
        }
    }

    private void addOrderTables(final List<OrderTable> orderTables, final TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(tableGroup.getId());
            orderTable.updateEmpty(false);
        }
    }

    @Override
    public void ungroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        orderManager.validateOrdersToUngroup(convertToIds(orderTables));
        ungroupOrderTables(orderTables);
    }

    private List<Long> convertToIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                          .map(OrderTable::getId)
                          .collect(Collectors.toList());
    }

    private void ungroupOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}
