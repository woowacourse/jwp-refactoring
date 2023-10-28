package kitchenpos.domain.table;

import kitchenpos.domain.order.Order;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class UngroupTableEventListener {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public UngroupTableEventListener(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(final UngroupTableEvent ungroupTableEvent) {
        final List<OrderTable> findOrderTables = orderTableRepository.findAllByTableGroupId(ungroupTableEvent.getTableGroup().getId());
        final OrderTables orderTables = new OrderTables(findOrderTables);

        validateCanUngroup(orderTables);

        orderTables.ungroupOrderTables();
    }

    private void validateCanUngroup(final OrderTables orderTables) {
        if (containsNotCompleteOrder(orderTables)) {
            throw new IllegalArgumentException("테이블 그룹을 해제하려면 그룹화된 테이블의 모든 주문이 완료 상태이어야 합니다.");
        }
    }

    private boolean containsNotCompleteOrder(final OrderTables orderTables) {
        final List<Order> findOrders = orderRepository.findAllByOrderTableIdIn(orderTables.getOrderTableIds());

        return findOrders.stream()
                         .anyMatch(Order::isNotComplete);
    }
}
