package kitchenpos.domain.order;

import kitchenpos.domain.order.exception.OrderException;
import kitchenpos.domain.orertable.OrderTable;
import kitchenpos.domain.orertable.OrderTableRepository;
import kitchenpos.domain.orertable.exception.OrderTableException.NotFoundOrderTableException;
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
