package kitchenpos.order.application;

import java.util.Arrays;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.application.TableOrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidator implements TableOrderStatusValidator {

    private final OrderRepository orderRepository;

    public OrderStatusValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderIsCompleted(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
