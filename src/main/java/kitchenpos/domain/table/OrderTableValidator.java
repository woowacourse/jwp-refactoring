package kitchenpos.domain.table;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(final OrderTables orderTables) {
        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final List<Order> savedOrders = orderRepository.findAllByOrderTableIds(orderTableIds);

        final boolean cannotUngroup = savedOrders.stream().anyMatch(Order::isProceeding);
        if (cannotUngroup) {
            throw new IllegalArgumentException("주문 상태가 식사중이거나 요리중이면 그룹을 해제할 수 없습니다.");
        }
    }
}
