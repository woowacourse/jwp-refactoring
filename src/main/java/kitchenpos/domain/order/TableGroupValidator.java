package kitchenpos.domain.order;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(final OrderTable orderTable) {
        if (isCanUngroupOrChangeEmpty(orderTable.getId())) {
            throw new IllegalArgumentException("[ERROR] 조리중이거나, 식사중인 테이블은 그룹을 해제할 수 없습니다.");
        }
    }

    private boolean isCanUngroupOrChangeEmpty(final Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
