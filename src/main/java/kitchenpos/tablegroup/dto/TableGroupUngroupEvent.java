package kitchenpos.tablegroup.dto;

public class TableGroupUngroupEvent {

    private final Long id;

    public TableGroupUngroupEvent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
