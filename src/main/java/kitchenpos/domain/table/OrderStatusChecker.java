package kitchenpos.domain.table;

import java.util.List;

public interface OrderStatusChecker {

    void validateOrderStatusChangeable(final List<Long> orderIds);

    void validateOrderStatusChangeableByTableGroupId(final Long tableGroupId);
}
