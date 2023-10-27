package kitchenpos.domain.table;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;

@Component
public class TableValidator {

    private final OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderStatus(final Long tableId) {
        final List<Order> orders = orderRepository.findByOrderByOrderTableId(tableId);
        final boolean containsNotCompletionOrder = orders.stream()
                .anyMatch(Order::isNotCompletionStatus);
        if (containsNotCompletionOrder) {
            throw new IllegalArgumentException("이미 주문이 진행 중이에요");
        }
    }
}
