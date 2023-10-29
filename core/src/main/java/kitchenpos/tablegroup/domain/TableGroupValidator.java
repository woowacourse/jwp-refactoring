package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.Order;
import kitchenpos.ordertable.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    @Autowired
    private final OrderTableRepository orderTableRepository;

    @Autowired
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroup create(List<OrderTable> orderTables) {
        validate(orderTables);
        return new TableGroup(null, LocalDateTime.now(), orderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        orderTables.stream()
            .filter(orderTable -> orderTable.getTableGroupId() != null)
            .findAny()
            .ifPresent(ignore -> {
                throw new IllegalArgumentException("해당 테이블은 이미 그룹화 되어있습니다.");
            });
    }

    public void ungroup(TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            extractId(tableGroup), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 있습니다.");
        }
        tableGroup.ungroup();
    }

    private List<Long> extractId(TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
            .flatMap(orderTable -> orderTable.getOrders().stream())
            .map(Order::getId)
            .collect(Collectors.toList());
    }
}
