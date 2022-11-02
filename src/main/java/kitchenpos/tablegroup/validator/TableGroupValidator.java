package kitchenpos.tablegroup.validator;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.tablegroup.exception.NotCompleteTableUngroupException;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderStatuses(List<Long> orderTableIds) {
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        boolean notCompletion = orders.stream()
                .anyMatch(Order::isNotCompletionOrderStatus);
        if (notCompletion) {
            throw new NotCompleteTableUngroupException();
        }
    }
}
