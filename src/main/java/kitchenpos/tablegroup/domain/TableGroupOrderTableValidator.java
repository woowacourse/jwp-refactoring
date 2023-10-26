package kitchenpos.tablegroup.domain;

import java.util.List;

public interface TableGroupOrderTableValidator {

    void validateOrderTablesByIds(final List<Long> orderTableIds);
}
