package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.order.domain.exception.OrderExceptionType;
import kitchenpos.table.application.TableChangeEmptyValidator;
import org.springframework.stereotype.Component;

@Component
public class TableValidator implements TableChangeEmptyValidator {

    private final OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateChangeEmpty(final Long orderTableId) {
        orderRepository.findByOrderTableId(orderTableId)
            .ifPresent(this::validateOrderIsNotCompletion);
    }

    private void validateOrderIsNotCompletion(final Order order) {
        if (order.isNotAlreadyCompletion()) {
            throw new OrderException(OrderExceptionType.ORDER_IS_NOT_COMPLETION);
        }
    }
}
