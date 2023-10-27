package kitchenpos.ordertable.application;

import kitchenpos.order.application.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repoisotory.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableOrderValidator implements OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public TableOrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
