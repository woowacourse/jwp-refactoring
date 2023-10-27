package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.OrderTableChangeEmptyEvent;
import kitchenpos.table.application.TableGroupCreateEvent;
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
