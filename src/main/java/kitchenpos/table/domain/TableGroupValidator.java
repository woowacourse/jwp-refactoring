package kitchenpos.table.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCreateGroup(List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹화를 위한 테이블이 2개 이상 필요합니다.");
        }

        for (OrderTable orderTable : orderTables) {
            validateTableStatus(orderTable);
        }
    }

    private void validateTableStatus(OrderTable foundOrderTable) {
        if (!foundOrderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 그룹화할 수 없습니다.");
        }

        if (foundOrderTable.isGrouped()) {
            throw new IllegalArgumentException("이미 그룹화된 테이블은 그룹화할 수 없습니다.");
        }
    }

    public void validateUnGroup(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리중이거나 식사중인 테이블이 존재합니다.");
        }
    }
}
