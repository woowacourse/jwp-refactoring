package kitchenpos.tablegroup.application;

import java.util.List;

public interface OrdersInTablesCompleteValidator {

    void validate(final List<Long> tableIds);
}
