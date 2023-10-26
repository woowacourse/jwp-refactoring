package kitchenpos.ordertable.service;

import kitchenpos.ordertable.domain.OrderTable;

public interface TableEmptyChangeService {

    void execute(OrderTable orderTable, boolean empty);
}
