package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateExistTable(final Long tableId) {
        final OrderTable orderTable = orderTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Order table does not exist."));
        validateOrderTableIsNotEmpty(orderTable);
    }

    private void validateOrderTableIsNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("Order from empty table is not allowed");
        }
    }
}
