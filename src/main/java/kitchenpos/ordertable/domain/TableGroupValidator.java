package kitchenpos.ordertable.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.exception.TableGroupException.CannotCreateTableGroupStateException;
import kitchenpos.ordertable.exception.TableGroupException.CannotUngroupStateByOrderStatusException;
import kitchenpos.ordertable.exception.TableGroupException.NotFoundOrderTableExistException;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCreate(final List<OrderTable> orderTables, final int orderTableSize,
                               final int foundOrderTableSize) {
        validateOrderTableSize(orderTableSize, foundOrderTableSize);
        validateOrderTablesStatus(orderTables);
    }

    private void validateOrderTableSize(final int orderTableSize, final int foundOrderTableSize) {
        if (orderTableSize != foundOrderTableSize) {
            throw new NotFoundOrderTableExistException();
        }
    }

    private void validateOrderTablesStatus(final List<OrderTable> orderTables) {
        orderTables.forEach(this::validateOrderTableStatus);
    }

    private void validateOrderTableStatus(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.isExistTableGroup()) {
            throw new CannotCreateTableGroupStateException();
        }
    }

    public void validateUnGroup(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new CannotUngroupStateByOrderStatusException();
        }
    }
}
