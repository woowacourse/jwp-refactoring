package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTableValidator;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.repository.order.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateAllOrderCompleted(final Long orderTableId) {
        orderRepository.findAllByOrderTableId(orderTableId)
                .forEach(this::validateOrderCompleted);
    }

    private void validateOrderCompleted(final Order order) {
        if (!order.isCompleted()) {
            throw new DomainLogicException(CustomError.UNCOMPLETED_ORDER_IN_TABLE_ERROR);
        }
    }
}
