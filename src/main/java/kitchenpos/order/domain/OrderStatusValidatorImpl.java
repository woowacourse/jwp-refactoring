package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.domain.OrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidatorImpl implements OrderStatusValidator {

    private final OrderRepository orderRepository;

    public OrderStatusValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateStatusChange(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 존재합니다.");
        }
    }
}
