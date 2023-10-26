package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupUngroupEvent {

    private final Long id;

    public TableGroupUngroupEvent(Long id) {
        this.id = id;
    }

    public static TableGroupUngroupEvent from(TableGroup tableGroup) {
        return new TableGroupUngroupEvent(tableGroup.getId());
    }

    public Long getId() {
        return id;
    }
}
