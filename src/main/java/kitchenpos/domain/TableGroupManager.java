package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupManager {

    private static final int MIN_GROUP_SIZE = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupManager(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public void group(final TableGroup tableGroup, final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = convertToOrderTables(orderTableIds);
        validateTableGroupAvailability(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.groupBy(tableGroup.getId());
        }
    }

    private List<OrderTable> convertToOrderTables(final List<Long> tableIds) {
        return tableIds.stream()
            .map(this::getOrderTable)
            .collect(Collectors.toList());
    }

    private OrderTable getOrderTable(final Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    private void validateTableGroupAvailability(final List<OrderTable> tables) {
        if (CollectionUtils.isEmpty(tables) || tables.size() < MIN_GROUP_SIZE) {
            throw new IllegalArgumentException("그룹으로 묶을 테이블은 2개 이상이어야 합니다.");
        }
        for (final OrderTable orderTable : tables) {
            validateTableAbleToGroup(orderTable);
        }
    }

    private void validateTableAbleToGroup(final OrderTable orderTable) {
        if (orderTable.isUnableToBeGrouped()) {
            throw new IllegalArgumentException("그룹으로 묶을 수 없는 테이블입니다.");
        }
    }

    @Transactional
    public void ungroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        final List<Long> orderTableIds = convertToOrderTableIds(orderTables);

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("종료된 주문이 아니라면 테이블 그룹을 해제할 수 없습니다.");
        }
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private List<Long> convertToOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}
