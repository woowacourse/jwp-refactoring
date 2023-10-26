package kitchenpos.domain.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table_group.TableGroupValidator;
import kitchenpos.support.AggregateReference;
import kitchenpos.exception.TableGroupException.CannotUngroupException;
import kitchenpos.exception.TableGroupException.NoMinimumOrderTableSizeException;
import kitchenpos.repositroy.OrderRepository;
import kitchenpos.repositroy.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidatorImpl implements TableGroupValidator {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupValidatorImpl(
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final List<AggregateReference<OrderTable>> orderTables) {
        final List<Long> orderTableIds = orderTables.stream().map(AggregateReference::getId)
                .collect(Collectors.toUnmodifiableList());

        if (orderTableRepository.countByIdIn(orderTableIds) < 2) {
            throw new NoMinimumOrderTableSizeException();
        }
    }

    @Override
    public void validateUnGroup(final AggregateReference<OrderTable> orderTable) {
        final Orders orders = new Orders(orderRepository.findByOrderTable(orderTable));

        if (orders.hasStatusOf(OrderStatus.COOKING) || orders.hasStatusOf(OrderStatus.MEAL)) {
            throw new CannotUngroupException();
        }
    }

}
