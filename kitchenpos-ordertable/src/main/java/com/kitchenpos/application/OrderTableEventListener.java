package com.kitchenpos.application;

import com.kitchenpos.event.message.ValidatorOrderTable;
import com.kitchenpos.exception.OrderTableEmptyException;
import com.kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEventListener {

    private final TableService tableService;

    public OrderTableEventListener(final TableService tableService) {
        this.tableService = tableService;
    }

    @EventListener
    private void validateOrderTable(final ValidatorOrderTable validatorOrderTable) {
        Long orderTableId = validatorOrderTable.getOrderTableId();

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
