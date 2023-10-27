package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrdersValidator;
import org.springframework.stereotype.Component;

@Component
public class OrdersValidatorImpl implements OrdersValidator {

    private final OrderRepository orderRepository;

    public OrdersValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusInOrderTable(OrderTable orderTable) {
        boolean canNotChange = orderRepository.findAllByOrderTableId(orderTable.getId())
                .stream()
                .anyMatch(Order::isCookingOrMeal);

        if (canNotChange) {
            throw new IllegalArgumentException("요리 중이거나 식사 중인 주문이 포함된 주문 테이블을 테이블 그룹에서 제외 시킬 수 없습니다.");
        }
    }

}
