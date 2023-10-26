package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableChangeEmptyEventListener {

    private final OrderRepository orderRepository;

    public OrderTableChangeEmptyEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void onApplicationEvent(final OrderTableChangeEmptyEvent event) {
        final List<Order> orders = orderRepository.findByOrderTableId(event.getOrderTableId());
        for (final Order order : orders) {
            if (order.isInProgress()) {
                throw new IllegalArgumentException("진행중인 주문이 있으면 해당 테이블을 비울 수 없습니다.");
            }
        }
    }
}
