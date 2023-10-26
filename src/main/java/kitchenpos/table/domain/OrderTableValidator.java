package kitchenpos.table.domain;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeStatusEmpty(OrderTable orderTable) {
        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException("주문 그룹에 속한 주문 테이블의 상태를 변경할 수 없습니다.");
        }

        if (hasNotCompletedOrder(orderTable)) {
            throw new IllegalArgumentException("테이블에 진행 중인 주문이 존재합니다.");
        }
    }

    private boolean hasNotCompletedOrder(OrderTable orderTable) {
        List<Order> foundOrders = orderRepository.findAllByOrderTableId(orderTable.getId());
        return !foundOrders.stream()
                .allMatch(Order::isCompleted);
    }

    public void validateChangingNumberOfGuests(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("EMPTY 상태인 주문 테이블의 손님 수를 변경할 수 없습니다.");
        }
    }
}
