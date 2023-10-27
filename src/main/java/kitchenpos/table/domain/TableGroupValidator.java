package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUngroup(final TableGroup tableGroup) {
        final boolean isNotAllCompleted = getOrders(tableGroup).stream().anyMatch(Order::isNotCompleted);
        if (isNotAllCompleted) {
            throw new IllegalArgumentException("주문 목록이 모두 완료된 상태가 아닌 경우 단체 지정을 해제할 수 없습니다.");
        }
    }

    private List<Order> getOrders(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .getOrderTableItems()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return orderRepository.findInOrderTableIds(orderTableIds);
    }
}
