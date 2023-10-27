package kitchenpos.table.application.request;

public class TableEmptyUpdateRequest {

    private Boolean empty;

    public TableEmptyUpdateRequest() {
    }

    public TableEmptyUpdateRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
