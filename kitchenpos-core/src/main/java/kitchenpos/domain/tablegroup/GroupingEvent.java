package kitchenpos.domain.tablegroup;

import kitchenpos.domain.tablegroup.TableGroup;

import java.util.List;

public class GroupingEvent {
    private final TableGroup tableGroup;
    private final List<Long> orderTableIds;

    public GroupingEvent(TableGroup tableGroup, List<Long> orderTableIds) {
        this.tableGroup = tableGroup;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
