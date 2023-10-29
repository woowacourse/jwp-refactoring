package kitchenpos.domain.order;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.orertable.OrderTable;
import kitchenpos.domain.orertable.TableGroupValidator;
import kitchenpos.domain.orertable.exception.TableGroupException.CannotCreateTableGroupStateException;
import kitchenpos.domain.orertable.exception.TableGroupException.CannotUngroupStateByOrderStatusException;
import kitchenpos.domain.orertable.exception.TableGroupException.NotFoundOrderTableExistException;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidatorImpl implements TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
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

    @Override
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
