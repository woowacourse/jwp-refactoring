package kitchenpos.order.infrastructure;

import java.util.Arrays;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderEmptyValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderEmptyValidatorImpl implements OrderEmptyValidator {

    private final OrderRepository orderRepository;

    public OrderEmptyValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("현재 조리 / 식사 중입니다.");
        }
    }
}
