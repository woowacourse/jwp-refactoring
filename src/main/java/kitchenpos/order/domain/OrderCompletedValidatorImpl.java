package kitchenpos.order.domain;

import java.util.Arrays;
import kitchenpos.table.domain.OrderCompletedValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderCompletedValidatorImpl implements OrderCompletedValidator {

    private final OrderRepository orderRepository;

    public OrderCompletedValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateCompleted(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않았습니다.");
        }
    }
}
