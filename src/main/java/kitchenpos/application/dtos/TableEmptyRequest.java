package kitchenpos.application.dtos;

public class TableEmptyRequest {
    private Boolean empty;

    public TableEmptyRequest() {
    }

    public TableEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
