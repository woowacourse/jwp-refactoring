package domain;

import exception.TableGroupException.CannotUngroupException;
import exception.TableGroupException.NoMinimumOrderTableSizeException;
import java.util.List;
import org.springframework.stereotype.Component;
import repository.OrderRepository;
import repository.OrderTableRepository;

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
    public void validate(final List<Long> orderTables) {
        if (orderTableRepository.countByIdIn(orderTables) < 2) {
            throw new NoMinimumOrderTableSizeException();
        }
    }

    @Override
    public void validateUnGroup(final Long orderTable) {
        final Orders orders = new Orders(orderRepository.findByOrderTableId(orderTable));

        if (orders.hasStatusOf(OrderStatus.COOKING) || orders.hasStatusOf(OrderStatus.MEAL)) {
            throw new CannotUngroupException();
        }
    }
}
