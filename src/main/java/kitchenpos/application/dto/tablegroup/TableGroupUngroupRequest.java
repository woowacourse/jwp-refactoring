package kitchenpos.application.dto.tablegroup;

public class TableGroupUngroupRequest {

    private Long id;

    TableGroupUngroupRequest() {

    }

    public TableGroupUngroupRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
