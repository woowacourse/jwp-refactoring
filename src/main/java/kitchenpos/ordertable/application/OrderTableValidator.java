package kitchenpos.ordertable.application;

import java.util.List;

public interface OrderTableValidator {

    void checkOrderComplete(final Long orderTableId);

    void checkOrderComplete(final List<Long> orderTableIds);
}
