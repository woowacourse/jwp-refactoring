package kitchenpos.domain.table;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.support.AggregateReference;
import kitchenpos.exception.TableException.NotCompletionTableCannotChangeEmptyException;
import kitchenpos.repositroy.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
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
