package kitchenpos.tablegroup.domain;

import java.util.List;

public interface TableGroupingService {

    void group(final List<Long> orderTableIds, final Long tableGroupId);

    void ungroup(final Long tableGroupId);
}
