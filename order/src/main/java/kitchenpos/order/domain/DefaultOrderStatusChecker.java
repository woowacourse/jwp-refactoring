package kitchenpos.order.domain;

import java.util.List;
import java.util.function.Predicate;
import kitchenpos.ordertable.domain.OrderStatusChecker;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Component
public class DefaultOrderStatusChecker implements OrderStatusChecker {

    private final OrderRepository orderRepository;

    public DefaultOrderStatusChecker(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean checkIncompleteOrders(Long orderTableId) {
        AggregateReference<OrderTable, Long> orderTableAggregateReference = AggregateReference.to(orderTableId);
        List<Order> tableOrders = orderRepository.findAllByOrderTableId(orderTableAggregateReference);

        return tableOrders.stream()
                .anyMatch(Predicate.not(Order::isComplete));

    }
}
