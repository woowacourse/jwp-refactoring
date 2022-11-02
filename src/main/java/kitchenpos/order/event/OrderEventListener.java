package kitchenpos.order.event;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.event.ChangeEmptyEvent;
import kitchenpos.table.event.UngroupEvent;

@Transactional(readOnly = true)
@Service
public class OrderEventListener {

    private static final List<OrderStatus> ORDER_UNCOMPLETED_STATUS = Arrays.asList(OrderStatus.COOKING,
        OrderStatus.MEAL);

    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener(UngroupEvent.class)
    public void validateOrderCompleted(UngroupEvent event) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(event.getOrderTables(), ORDER_UNCOMPLETED_STATUS)) {
            throw new IllegalArgumentException("주문이 완료되지 않아서 그룹을 해제할 수 없습니다.");
        }
    }

    @EventListener(ChangeEmptyEvent.class)
    public void validateOrderCompleted(ChangeEmptyEvent event) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(event.getOrderTable(), ORDER_UNCOMPLETED_STATUS)) {
            throw new IllegalArgumentException("주문이 완료되지 않았습니다.");
        }
    }
}
