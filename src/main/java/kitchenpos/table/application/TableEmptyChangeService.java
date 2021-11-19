package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;

public interface TableEmptyChangeService {

    boolean canChangeEmpty(final OrderTable orderTable);
}
