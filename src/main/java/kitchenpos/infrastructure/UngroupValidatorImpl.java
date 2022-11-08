package kitchenpos.infrastructure;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.UngroupValidator;
import org.springframework.stereotype.Component;

@Component
public class UngroupValidatorImpl implements UngroupValidator {

    private final OrderRepository orderRepository;

    public UngroupValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(List<Long> orderTableIds) {
        for (Long orderTableId : orderTableIds) {
            validateOrderTable(orderTableId);
        }
    }

    private void validateOrderTable(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태입니다");
        }
    }
}
