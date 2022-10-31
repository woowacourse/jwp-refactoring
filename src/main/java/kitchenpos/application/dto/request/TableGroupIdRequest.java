package kitchenpos.application.dto.request;

public class TableGroupIdRequest {

    private final Long id;

    private TableGroupIdRequest() {
        this(null);
    }

    public TableGroupIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
