package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.domain.OrderTable;

import java.util.List;

public interface OrderCompletionValidator {
    
    public void validateIfOrderOfOrderTableIsCompleted(final List<OrderTable> orderTables, Long tableId);
}
