package domain;

import exception.TableException.NotCompletionTableCannotChangeEmptyException;
import org.springframework.stereotype.Component;
import repository.OrderRepository;
import support.AggregateReference;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(final OrderTable orderTable) {
        final AggregateReference<OrderTable> orderTableId = new AggregateReference<>(orderTable.getId());
        final Orders orders = new Orders(orderRepository.findByOrderTableId(orderTableId.getId()));

        if (orders.isEmpty() || orders.isAllStatusOf(OrderStatus.COMPLETION)) {
            return;
        }

        throw new NotCompletionTableCannotChangeEmptyException();
    }
}
