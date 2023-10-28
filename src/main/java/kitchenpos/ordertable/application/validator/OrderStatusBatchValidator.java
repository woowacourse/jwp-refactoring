package kitchenpos.ordertable.application.validator;

import kitchenpos.ordertable.OrderTables;

public interface OrderStatusBatchValidator {

    void batchValidateCompletion(final OrderTables orderTables);
}
