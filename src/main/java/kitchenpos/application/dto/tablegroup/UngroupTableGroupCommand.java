package kitchenpos.application.dto.tablegroup;

public class UngroupTableGroupCommand {

    private final Long tableGroupId;

    public UngroupTableGroupCommand(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long tableGroupId() {
        return tableGroupId;
    }
}
