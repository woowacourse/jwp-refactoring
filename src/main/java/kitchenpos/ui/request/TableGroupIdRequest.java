package kitchenpos.ui.request;

public class TableGroupIdRequest {
    private final Long id;

    public TableGroupIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
