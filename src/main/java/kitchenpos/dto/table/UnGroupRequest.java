package kitchenpos.dto.table;

public class UnGroupRequest {
    private final Long tableGroupId;

    private UnGroupRequest(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public static UnGroupRequest of(final Long tableGroupId) {
        return new UnGroupRequest(tableGroupId);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
