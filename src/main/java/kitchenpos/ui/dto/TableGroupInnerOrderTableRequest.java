package kitchenpos.ui.dto;

public class TableGroupInnerOrderTableRequest {

    private Long id;

    private TableGroupInnerOrderTableRequest() {
    }

    public TableGroupInnerOrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
