package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(final OrderRepository orderRepository,
                               final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateUngroup(final Long tableGroupId) {
        if (isNotAllCompleted(getOrders(tableGroupId))) {
            throw new IllegalArgumentException("주문 목록이 모두 완료된 상태가 아닌 경우 단체 지정을 해제할 수 없습니다.");
        }
    }

    private boolean isNotAllCompleted(final List<Order> orders) {
        return orders
                .stream()
                .anyMatch(Order::isNotCompleted);
    }

    private List<Order> getOrders(final Long tableGroupId) {
        final List<Long> orderTableIds = orderTableRepository.findByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return orderRepository.findInOrderTableIds(orderTableIds);
    }
}
