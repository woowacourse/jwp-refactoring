package kitchenpos.domain.tablegroup;

import java.util.List;

public interface GroupingTableService {

    void group(final List<Long> orderTableIds, final TableGroup tableGroup);

    void ungroup(final TableGroup tableGroup);
}
