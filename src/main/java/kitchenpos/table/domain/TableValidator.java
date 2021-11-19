package kitchenpos.table.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(OrderTable orderTable) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        boolean isCookingOrMeal = orders.stream()
                                        .anyMatch(order -> order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL));
        if (isCookingOrMeal) {
            throw new IllegalArgumentException("주문 상태가 조리중이나 식사중입니다.");
        }
    }
}
