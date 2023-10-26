package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;

public class TableGroupCreatedEvent {
    private final Long id;
    private final List<Long> orderTableIds;

    public TableGroupCreatedEvent(Long id, List<Long> orderTableIds) {
        this.id = id;
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupCreatedEvent from(TableGroup tableGroup) {
        return new TableGroupCreatedEvent(tableGroup.getId(), tableGroup.getOrderTableIds());
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
