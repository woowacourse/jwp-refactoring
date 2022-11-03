package kitchenpos.ordertable.application;

import java.util.List;

public interface OrderTableValidator {

    void checkOrderCompleted(final Long orderTableId);

    void checkOrderCompleted(final List<Long> orderTableIds);
}
