package order.application;

import order.domain.Orders;
import order.domain.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import table.domain.OrdersValidatedEvent;

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
