package kitchenpos.ordertable.application;

import kitchenpos.order.application.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
