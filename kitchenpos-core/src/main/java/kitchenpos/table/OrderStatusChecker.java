package kitchenpos.table;

import java.util.List;

public interface OrderStatusChecker {

    void validateOrderStatusChangeable(final List<Long> orderTableIds);

    boolean checkEnableUngroupingTableGroup(final Long tableGroupId);
}
