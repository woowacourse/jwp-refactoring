package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(final Long orderTableId) {
        validateEmptyIsNotChangeable(orderTableId);
    }

    private void validateEmptyIsNotChangeable(final Long orderTableId) {
        final Optional<Order> maybeOrder = orderRepository.findByOrderTableId(orderTableId);
        if (maybeOrder.isEmpty()) {
            return;
        }

        final Order findOrder = maybeOrder.get();
        if (findOrder.isNotCompleted()) {
            throw new IllegalArgumentException(String.format("현재 주문이 %s 상태 이므로 주문 테이블의 빈 상태를 변경할 수 없습니다", findOrder.getOrderStatus()));
        }
    }
}
