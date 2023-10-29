package kitchenpos.dto;

public class TableGroupOrderTableRequest {

    private Long id;

    protected TableGroupOrderTableRequest() {
    }

    public TableGroupOrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
