package kitchenpos.ordertable;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoOngoingOrderValidator {

    private final OrderRepository orderRepository;

    public NoOngoingOrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(OrderTable orderTable) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
        validateNoOngoing(orders);
    }

    private void validateNoOngoing(List<Order> orders) {
        if (hasAnyOngoing(orders)) {
            throw new IllegalArgumentException("이미 진행중인 주문이 있습니다");
        }
    }

    private boolean hasAnyOngoing(List<Order> orders) {
        return orders.stream()
                .anyMatch(Order::isOngoing);
    }
}
