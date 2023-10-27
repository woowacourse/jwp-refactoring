package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableChangeEmptyValidator {

    private final OrderRepository orderRepository;

    public OrderTableChangeEmptyValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(final OrderTable orderTable) {
        if (hasOrderNotCompleted(orderTable.getId())) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 비어있는 상태를 변경할 수 없습니다.");
        }
    }

    private boolean hasOrderNotCompleted(final Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        );
    }
}
