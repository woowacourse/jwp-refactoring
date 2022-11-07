package kitchenpos.domain;

import kitchenpos.domain.table.OrderTableValidator;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

public class FalseOrderTableValidator implements OrderTableValidator {

    @Override
    public void validateAllOrderCompleted(final Long orderTableId) {
        throw new DomainLogicException(CustomError.UNCOMPLETED_ORDER_IN_TABLE_ERROR);
    }
}
