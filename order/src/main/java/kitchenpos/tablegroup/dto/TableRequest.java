package kitchenpos.tablegroup.dto;

public class TableRequest {
    private Long id;

    protected TableRequest() {
    }

    public TableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
