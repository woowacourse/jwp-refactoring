package kitchenpos.application;

import java.util.Arrays;
import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중이거나 식사 상태입니다.");
        }
    }
}
