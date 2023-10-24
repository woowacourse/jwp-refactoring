package kitchenpos.domain.table;

import java.util.List;

public interface OrderStatusChecker {

    void validateOrderStatusChangeable(final List<Long> orderIds);

    boolean checkUngroupableTableGroup(final Long tableGroupId);
}
