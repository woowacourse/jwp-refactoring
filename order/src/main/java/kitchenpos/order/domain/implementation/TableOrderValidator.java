package kitchenpos.order.domain.implementation;

import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.order.domain.exception.OrderExceptionType;
import kitchenpos.table.domain.TableChangeEmptyValidator;
import org.springframework.stereotype.Component;

@Component
public class TableOrderValidator implements TableChangeEmptyValidator {

    private final OrderRepository orderRepository;

    public TableOrderValidator(final OrderRepository orderRepository) {
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
