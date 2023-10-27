package kitchenpos.tablegroup.dto;

public class TableGroupUngroupEvent {

    private final Long tableGroupId;

    public TableGroupUngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
