package kitchenpos.order.application;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrdersValidatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private OrderRepository orderRepository;

    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validatedOrders(OrdersValidatedEvent ordersValidatedEvent) {
        Orders orders = new Orders(orderRepository.findAllByOrderTableId(ordersValidatedEvent.getOrderTableId()));
        orders.validateChangeEmpty();
    }
}
