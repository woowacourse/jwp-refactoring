package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;

import java.util.List;

public interface TableValidator {
    void validateTableChangeEmpty(OrderTable orderTable);

    void validateUnGroupable(List<Long> orderTableIds);
}
