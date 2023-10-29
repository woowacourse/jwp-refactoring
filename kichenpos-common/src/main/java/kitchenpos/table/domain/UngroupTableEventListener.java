package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.tablegroup.domain.UngroupTableEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UngroupTableEventListener {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public UngroupTableEventListener(
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(final UngroupTableEvent ungroupTableEvent) {
        final List<OrderTable> findOrderTables =
                orderTableRepository.findAllByTableGroupId(ungroupTableEvent.getTableGroup().getId());

        validateCanUngroup(findOrderTables);

        ungroupOrderTables(findOrderTables);
    }

    private void validateCanUngroup(final List<OrderTable> orderTables) {
        if (containsNotCompleteOrder(orderTables)) {
            throw new IllegalArgumentException("테이블 그룹을 해제하려면 그룹화된 테이블의 모든 주문이 완료 상태이어야 합니다.");
        }
    }

    private boolean containsNotCompleteOrder(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());
        final List<Order> findOrders = orderRepository.findAllByOrderTableIdIn(orderTableIds);

        return findOrders.stream()
                         .anyMatch(Order::isNotComplete);
    }

    private void ungroupOrderTables(final List<OrderTable> orderTables) {
        validateCanUngroup(orderTables);

        if (containsDifferentId(orderTables)) {
            throw new IllegalArgumentException("다른 테이블 그룹에 속한 테이블이 포함되어 있습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }

    }

    private boolean containsDifferentId(final List<OrderTable> orderTables) {
        final long orderTableIdCount = orderTables.stream()
                                                  .map(OrderTable::getTableGroupId)
                                                  .distinct()
                                                  .count();

        return orderTableIdCount != 1;
    }
}
