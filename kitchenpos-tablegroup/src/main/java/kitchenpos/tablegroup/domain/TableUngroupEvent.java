package kitchenpos.tablegroup.domain;

public class TableUngroupEvent {

    private final Long ungroupTableId;

    public TableUngroupEvent(final Long ungroupTableId) {
        this.ungroupTableId = ungroupTableId;
    }

    public Long getUngroupTableId() {
        return ungroupTableId;
    }
}
