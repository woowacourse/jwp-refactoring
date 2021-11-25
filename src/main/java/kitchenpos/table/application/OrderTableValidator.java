package kitchenpos.table.application;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrders(Long orderTableId) {
        Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
        orders.validateCompleted();
    }
}
