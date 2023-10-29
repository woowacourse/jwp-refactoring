package kitchenpos.table.domain;

import kitchenpos.configuration.Validator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;

import java.util.List;

@Validator
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCanChangeEmpty(final OrderTable orderTable) {
        if (containsNotCompleteOrder(orderTable.getId())) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 주문이 남아있다면 테이블 상태를 변경할 수 없습니다.");
        }

        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException("그룹화된 테이블의 상태를 변경할 수 없습니다.");
        }
    }

    private boolean containsNotCompleteOrder(final Long orderTableId) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);

        return orders.stream()
                     .anyMatch(Order::isNotComplete);
    }
}
