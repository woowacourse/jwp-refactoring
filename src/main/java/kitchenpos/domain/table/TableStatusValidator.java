package kitchenpos.domain.table;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TableStatusValidator implements TableValidator {
    private final OrderRepository orderRepository;

    public TableStatusValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 상태가 완료가 아닙니다");
        }
    }
}
