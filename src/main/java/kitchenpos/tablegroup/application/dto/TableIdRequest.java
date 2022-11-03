package kitchenpos.tablegroup.application.dto;

public class TableIdRequest {

    private final Long id;

    public TableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
