package kitchenpos.ui.request;

public class TableUpdateEmptyRequest {

    private Boolean empty;

    public TableUpdateEmptyRequest(final Boolean empty) {
        this.empty = empty;
    }

    public TableUpdateEmptyRequest() {
    }

    public Boolean getEmpty() {
        return empty;
    }
}
