package core.domain;

import org.springframework.data.jdbc.core.mapping.AggregateReference;
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
        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                AggregateReference.to(orderTable.getId()), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 상태가 완료가 아닙니다");
        }
    }
}
