package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private static final List<OrderStatus> UNCHANGEABLE_STATUS = List.of(OrderStatus.MEAL, OrderStatus.COOKING);

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUnableUngrouping(List<OrderTable> orderTables) {
        List<Long> orderTableIds = extractOrderTableIds(orderTables);

        if (isUnableUngrouping(orderTableIds)) {
            throw new IllegalArgumentException("주문이 완료되지 않은 상태의 테이블이 존재합니다.");
        }
    }

    private List<Long> extractOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private boolean isUnableUngrouping(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, UNCHANGEABLE_STATUS);
    }

}
