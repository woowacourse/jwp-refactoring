package kitchenpos.tablegroup;

import java.util.List;

public interface TableGrouping {

    void group(TableGroup tableGroup, List<Long> orderTableIds);

    void ungroup(TableGroup tableGroup);
}
