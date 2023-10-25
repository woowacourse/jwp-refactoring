package kitchenpos.domain.tablegroup.service;

import java.util.List;
import kitchenpos.domain.tablegroup.TableGroup;

public interface GroupingTableService {

    void group(final List<Long> orderTableIds, final TableGroup tableGroup);

    void ungroup(final TableGroup tableGroup);
}
