package kitchenpos.dto.request.tablegroup;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }
}
