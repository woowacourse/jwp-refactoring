package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderValidator;
import kitchenpos.ordertable.exception.OrderTableException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderStatusValidator implements OrderValidator {

    private OrderRepository orderRepository;

    public OrderStatusValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrder(Long orderTableId) {
        List<Order> orders = orderRepository.findByOrderTableId(orderTableId);
        if (existsNotCompletedOrder(orders)) {
            throw new OrderTableException("주문테이블에 속한 주문이 요리중 또는 식사중이므로 상태를 변경할 수 없습니다.");
        }
    }

    private boolean existsNotCompletedOrder(List<Order> orders) {
        return orders.stream()
                .noneMatch(Order::isCompleted);
    }
}
