package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.exception.TableGroupException;
import kitchenpos.tablegroup.exception.TableGroupException.CannotCreateTableGroupStateException;

public class TableGroupValidator {

    public static void validateOrderTableSize(final int orderTableSize, final int foundOrderTableSize) {
        if (orderTableSize != foundOrderTableSize) {
            throw new TableGroupException.NotFoundOrderTableExistException();
        }
    }

    public static void validateOrderTableStatus(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.isExistTableGroup()) {
            throw new CannotCreateTableGroupStateException();
        }
    }
}
