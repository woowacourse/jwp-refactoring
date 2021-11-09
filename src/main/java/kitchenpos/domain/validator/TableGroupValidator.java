package kitchenpos.domain.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUngroup(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
