package kitchenpos.order.event;

public class UngroupEvent {

    private final Long tableGroupId;

    public UngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
