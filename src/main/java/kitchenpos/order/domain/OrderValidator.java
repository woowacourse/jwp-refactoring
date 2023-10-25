package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.OrderTableException.NotFoundOrderTableException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreate(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);

        if (orderTable.isEmpty()) {
            throw new OrderException.CannotOrderStateByOrderTableEmptyException();
        }
    }
}
