package kitchenpos.domain.order;

import static kitchenpos.domain.vo.OrderStatus.COOKING;
import static kitchenpos.domain.vo.OrderStatus.MEAL;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderRepository orderRepository;

    public OrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateHasAnyOrderInProgress(final long orderTableId) {
        if (hasAnyOrderInProgress(orderTableId)) {
            throw new IllegalArgumentException("이미 진행 중인 주문이 존재합니다.");
        }
    }

    private boolean hasAnyOrderInProgress(final long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(COOKING, MEAL)
        );
    }
}
