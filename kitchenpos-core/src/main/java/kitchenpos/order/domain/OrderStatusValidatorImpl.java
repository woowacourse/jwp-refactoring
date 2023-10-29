package kitchenpos.order.domain;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderStatusValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderStatusValidatorImpl implements OrderStatusValidator {

    private OrderRepository orderRepository;

    public OrderStatusValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void checkIfOrderIsNotCompleted(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
