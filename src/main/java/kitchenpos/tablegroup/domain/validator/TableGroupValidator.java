package kitchenpos.tablegroup.domain.validator;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TableGroupValidator {
    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCompletedTableGroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        final List<OrderStatus> inProgressingOrderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, inProgressingOrderStatuses)) {
            throw new IllegalArgumentException();
        }
    }
}
