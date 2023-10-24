package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.Objects;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableException;
import kitchenpos.table.domain.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new OrderTableException("그룹에 속한 테이블은 비어있음 상태를 변경할 수 없습니다.");
        }
        validateUngroup(orderTable, "조리 혹은 식사중 상태의 테이블의 비어있음 상태는 변경할 수 없습니다.");
    }

    public void validateUngroup(OrderTable orderTable, String errorMessage) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(),
                List.of(COOKING.name(), MEAL.name()))
        ) {
            throw new OrderTableException(errorMessage);
        }
    }
}
