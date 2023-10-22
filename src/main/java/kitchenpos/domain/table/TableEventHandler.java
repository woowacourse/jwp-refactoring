package kitchenpos.domain.table;

import kitchenpos.dao.table.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableEventHandler {

    private final OrderTableRepository orderTableRepository;

    public TableEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handle(final TableValidationEvent event) {
        final Long tableId = event.getTableId();
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
