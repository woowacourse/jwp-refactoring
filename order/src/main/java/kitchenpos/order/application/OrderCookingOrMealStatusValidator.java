package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.vo.OrdersValidator;
import kitchenpos.table.domain.OrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderCookingOrMealStatusValidator implements OrderStatusValidator {

    private final OrderRepository orderRepository;

    public OrderCookingOrMealStatusValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        OrdersValidator ordersValidator = new OrdersValidator(orders);

        ordersValidator.validateHasCookingOrMealOrder();
    }
}
