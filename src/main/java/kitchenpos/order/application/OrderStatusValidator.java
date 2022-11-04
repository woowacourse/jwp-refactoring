package kitchenpos.order.application;

import java.util.List;

public interface OrderStatusValidator {

    boolean existsByIdAndStatusNotCompletion(final Long orderTableId);

    boolean existsByOrderTableIdInAndStatusNotCompletion(final List<Long> orderTableIds);

}
