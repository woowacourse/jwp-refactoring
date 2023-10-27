package kitchenpos.order.domain.service;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderRepository orderRepository;

    public OrderValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCompletedOrders(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validateCompletedOrders(orderTable.getId());
        }
    }

    public void validateCompletedOrders(final Long orderTableId) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        validateComplete(orders);
    }

    private void validateComplete(List<Order> orders) {
        if (!orders.stream().allMatch(Order::isCompleted)) {
            throw new IllegalArgumentException("현재 주문이 계산 완료되지 않았습니다.");
        }
    }
}
