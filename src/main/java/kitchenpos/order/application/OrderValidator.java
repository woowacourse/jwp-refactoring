package kitchenpos.order.application;

import java.util.Arrays;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.Validator;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator implements Validator {

    private final OrderRepository orderRepository;

    public OrderValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatus(final OrderTable orderTable) {
        final boolean isExist = orderRepository.existsByOrderTableAndOrderStatusIn(orderTable,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
        if (isExist) {
            throw new IllegalArgumentException("주문이 요리중이거나 식사중이여서 변경할 수 없습니다.");
        }
    }
}
