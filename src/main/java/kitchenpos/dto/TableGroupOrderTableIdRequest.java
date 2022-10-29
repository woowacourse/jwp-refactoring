package kitchenpos.dto;

public class TableGroupOrderTableIdRequest {

    private final Long id;

    public TableGroupOrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
