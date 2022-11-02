package kitchenpos.order.event;

import java.util.Arrays;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.event.UngroupEvent;

@Transactional(readOnly = true)
@Service
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener(UngroupEvent.class)
    public void validateOrderCompleted(UngroupEvent ungroupEvent) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            ungroupEvent.getOrderTables(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않아서 그룹을 해제할 수 없습니다.");
        }
    }
}
