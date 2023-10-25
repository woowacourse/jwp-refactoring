package kitchenpos.table.validator;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import kitchenpos.table.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableChangeEmptyValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableChangeEmptyValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Long orderTableId) {
        if (orderTableRepository.getById(orderTableId).isGrouped()) {
            throw new IllegalArgumentException("그룹된 테이블을 비울 수 없습니다.");
        }

        if (isAnyOrderInProgress(orderTableId)) {
            throw new IllegalArgumentException("조리중 또는 식사중인 테이블은 비울 수 없습니다.");
        }
    }

    private boolean isAnyOrderInProgress(Long orderTableId) {
        return orderRepository.findAllByOrderTableIdIn(List.of(orderTableId)).stream()
                .anyMatch(Order::isCookingOrMeal);
    }
}
