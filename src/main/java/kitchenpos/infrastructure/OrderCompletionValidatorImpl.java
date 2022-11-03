package kitchenpos.infrastructure;

import java.util.Arrays;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderCompletionValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderCompletionValidatorImpl implements OrderCompletionValidator {

    private final OrderRepository orderRepository;

    public OrderCompletionValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리 중 혹은 식사 중인 테이블입니다");
        }
    }
}
