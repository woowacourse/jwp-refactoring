package kitchenpos.domain.tablegroup;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUngroup(TableGroup tableGroup) {
        for (final OrderTable orderTable : tableGroup.getOrderTables()) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        final Optional<Order> order = orderRepository.findByOrderTableId(orderTable.getId());
        if (order.isEmpty()) {
            return;
        }
        if (order.get().getOrderStatus() == OrderStatus.COOKING || order.get().getOrderStatus() == OrderStatus.MEAL) {
            throw new IllegalArgumentException("주문 상태가 조리중 또는 식사중인 테이블은 단체지정을 해제할 수 없습니다.");
        }
    }
}
