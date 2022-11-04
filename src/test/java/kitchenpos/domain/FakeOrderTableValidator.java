package kitchenpos.domain;

import java.util.List;
import kitchenpos.table.application.OrderTableValidator;

public class FakeOrderTableValidator implements OrderTableValidator {

    @Override
    public void validateCompletionStatus(final Long orderTableId) {
        // do nothing
    }

    @Override
    public void validateAllCompletionStatus(final List<Long> orderTableIds) {
        // do nothing
    }
}
