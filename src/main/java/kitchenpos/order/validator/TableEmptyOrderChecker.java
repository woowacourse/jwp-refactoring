package kitchenpos.order.validator;

import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.validator.OrderChecker;
import org.springframework.stereotype.Component;

@Component
public class TableEmptyOrderChecker implements OrderChecker {

    private final OrderRepository orderRepository;

    public TableEmptyOrderChecker(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean isNotCompletionOrder(Long orderTableId) {
        Optional<Order> order = orderRepository.findByOrderTableId(orderTableId);
        return order.isPresent() && order.get().isNotCompletionOrderStatus();
    }
}
