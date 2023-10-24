package kitchenpos.table.domain;

import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private static final List<OrderStatus> UNCHANGEABLE_STATUS = List.of(OrderStatus.MEAL, OrderStatus.COOKING);

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeableEmpty(Long orderTableId) {
        if (isUnableChangeEmpty(orderTableId)) {
            throw new IllegalArgumentException("Completion 상태가 아닌 주문 테이블은 주문 가능 여부를 변경할 수 없습니다.");
        }
    }

    private boolean isUnableChangeEmpty(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, UNCHANGEABLE_STATUS);
    }

}
