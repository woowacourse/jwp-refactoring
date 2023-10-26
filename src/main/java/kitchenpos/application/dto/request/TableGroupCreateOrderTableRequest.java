package kitchenpos.application.dto.request;

public class TableGroupCreateOrderTableRequest {

    private Long id;

    public TableGroupCreateOrderTableRequest() {
    }

    public TableGroupCreateOrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
