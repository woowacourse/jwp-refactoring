package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidator {

    public static final int MIN_ORDER_TABLE_SIZE = 2;

    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(final List<OrderTable> orderTables) {
        validateNumberOfOrderTable(orderTables);
        validateOrderTableStatus(orderTables);
    }

    private void validateNumberOfOrderTable(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("order table 수는 2 이상이어야 합니다.");
        }
    }

    private void validateOrderTableStatus(final List<OrderTable> orderTables) {
        orderTables.stream()
                .filter(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()))
                .findAny()
                .ifPresent(orderTable -> {
                    throw new IllegalArgumentException("orderTable 은 비어있어야 하고, 소속된 table group이 없어야 합니다.");
                });
    }

    public void validateOrderStatus(final List<OrderTable> orderTables) {
        final List<OrderStatus> statuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, statuses)) {
            throw new IllegalArgumentException("주문 상태가 MEAL, COOKING 이면 그룹을 해제할 수 없습니다.");
        }
    }
}
