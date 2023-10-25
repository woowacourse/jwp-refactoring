package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.application.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateChangeEmpty(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            List.of(OrderStatus.MEAL, OrderStatus.COOKING))) {
            throw new IllegalArgumentException("주문이 완료된 테이블만 상태를 변경할 수 있습니다.");
        }
    }
}
