package kitchenpos.table.application;

import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidatorImpl implements TableValidator {
    private final OrderTableRepository orderTableRepository;

    public TableValidatorImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }


    @Override
    public void validateTableNotExists(final Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new OrderTableNotFoundException();
        }
    }

    public void validateTableEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }
}
