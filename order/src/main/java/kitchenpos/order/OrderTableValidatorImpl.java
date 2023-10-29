package kitchenpos.order;

import kitchenpos.ordertable.OrderTableValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {
    private OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmptyTableOrderCondition(final Long orderTableId) {
        final List<Order> tableOrders = orderRepository.findByOrderTableId(orderTableId);

        for (Order order : tableOrders) {
            if (order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL)) {
                throw new IllegalArgumentException("테이블의 주문이 조리, 혹은 식사 상태이면 빈 테이블로 변경할 수 없습니다.");
            }
        }
    }
}
