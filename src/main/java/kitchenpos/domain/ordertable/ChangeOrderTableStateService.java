package kitchenpos.domain.ordertable;

import kitchenpos.domain.ordertable.OrderTable;

public interface ChangeOrderTableStateService {

    void changeEmpty(final OrderTable orderTable, final boolean empty);
}
