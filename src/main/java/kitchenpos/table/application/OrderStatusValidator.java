package kitchenpos.table.application;

import java.util.List;
import kitchenpos.product.domain.OrderStatus;

public interface OrderStatusValidator {

    boolean existsByIdAndStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);

    boolean existsByIdInAndStatusIn(final List<Long> orderTableIds, final List<OrderStatus> orderStatuses);
}
