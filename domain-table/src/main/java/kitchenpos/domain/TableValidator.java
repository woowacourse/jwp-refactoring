package kitchenpos.domain;

import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrder(OrderTable orderTable) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        boolean isCookingOrMeal = orders.stream()
                                        .anyMatch(order -> order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL));
        if (isCookingOrMeal) {
            throw new IllegalArgumentException("주문 상태가 조리중이나 식사중입니다.");
        }
    }
}
