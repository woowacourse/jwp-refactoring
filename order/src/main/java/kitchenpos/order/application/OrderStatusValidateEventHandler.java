package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.vo.OrdersValidator;
import kitchenpos.table.domain.OrderStatusValidateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidateEventHandler {

    private final OrderRepository orderRepository;

    public OrderStatusValidateEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateTableHasCookingOrMealOrder(OrderStatusValidateEvent event) {
        List<Order> orders = orderRepository.findAllByOrderTableId(event.getOrderTableId());
        OrdersValidator ordersValidator = new OrdersValidator(orders);

        ordersValidator.validateHasCookingOrMealOrder();
    }
}
