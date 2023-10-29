package tablegroups.dto;

public class UnGroupRequest {
    private Long tableGroupId;

    private UnGroupRequest(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public UnGroupRequest() {
    }

    public static UnGroupRequest of(final Long tableGroupId) {
        return new UnGroupRequest(tableGroupId);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
