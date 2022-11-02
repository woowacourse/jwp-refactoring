package kitchenpos.domain.ordertable.validator;

import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;

public interface OrderTableValidator {
    void validateAbleToChangeEmpty(boolean empty, OrderTable orderTable);
    void validateAbleToUngroup(final TableGroup tableGroup);
}
