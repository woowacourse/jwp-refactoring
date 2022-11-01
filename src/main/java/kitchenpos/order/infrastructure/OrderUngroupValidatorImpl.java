package kitchenpos.order.infrastructure;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderUngroupValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderUngroupValidatorImpl implements OrderUngroupValidator {

    private final OrderRepository orderRepository;

    public OrderUngroupValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatus(final List<Long> ids) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(ids,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 그룹을 분리할 수 없습니다.");
        }
    }
}
