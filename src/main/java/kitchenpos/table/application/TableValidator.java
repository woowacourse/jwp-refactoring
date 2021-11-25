package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrder(OrderTable orderTable) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        orders.stream()
                .filter(Order::isNotCompletion)
                .findAny()
                .ifPresent(order -> {
                    throw new IllegalArgumentException("orderId : " + order.getId() + "인 Order가 Completion 상태가 아닙니다.");
                });
    }
}
