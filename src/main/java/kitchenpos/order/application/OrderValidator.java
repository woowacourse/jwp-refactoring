package kitchenpos.order.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }


    public void validate(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
