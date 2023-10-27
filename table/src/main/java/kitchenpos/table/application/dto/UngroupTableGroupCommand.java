package kitchenpos.table.application.dto;

public class UngroupTableGroupCommand {

    private final Long tableGroupId;

    public UngroupTableGroupCommand(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long tableGroupId() {
        return tableGroupId;
    }
}
