package kitchenpos.common.event.listener;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.exception.OrderTableEmptyException;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEventListener {

    private final TableService tableService;

    public OrderTableEventListener(final TableService tableService) {
        this.tableService = tableService;
    }

    public void validateOrderTable(final Long orderTableId) {
        validateOrderTableExistById(orderTableId);
        validateOrderTableEmptyById(orderTableId);
    }

    private void validateOrderTableExistById(final Long orderTableId) {
        if (!tableService.isExistById(orderTableId)) {
            throw new OrderTableNotFoundException();
        }
    }

    private void validateOrderTableEmptyById(final Long orderTableId) {
        if (!tableService.isExistsByIdAndEmptyIsFalse(orderTableId)) {
            throw new OrderTableEmptyException();
        }
    }
}
