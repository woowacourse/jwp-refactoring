package kitchenpos.core.order.application;

import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.repository.OrderRepository;
import kitchenpos.core.table.application.OrderTableChangeEmptyEvent;
import kitchenpos.core.table.application.TableGroupCreateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableEventHandler {

    private final OrderRepository orderRepository;

    public TableEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void onApplicationEvent(final OrderTableChangeEmptyEvent event) {
        final List<Order> orders = orderRepository.findByOrderTableId(event.getOrderTableId());
        if (orders.stream().anyMatch(Order::isInProgress)) {
            throw new IllegalArgumentException("진행중인 주문이 있으면 해당 테이블을 비울 수 없습니다.");
        }

    }

    @EventListener
    public void onApplicationEvent(final TableGroupCreateEvent event) {
        final List<Order> orders = orderRepository.findByOrderTableId(event.getOrderTableId());
        if (orders.stream().anyMatch(Order::isInProgress)) {
            throw new IllegalArgumentException("진행중인 주문이 있으면 해당 테이블을 비울 수 없습니다.");
        }

    }
}
