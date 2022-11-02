package kitchenpos.order.event;

public class UngroupEvent {

    private Long tableGroupId;

    public UngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
