package kitchenpos.order.ui.request;

public class TableUpdateEmptyRequest {

    private Boolean empty;

    public TableUpdateEmptyRequest(final Boolean empty) {
        this.empty = empty;
    }

    protected TableUpdateEmptyRequest() {
    }

    public Boolean getEmpty() {
        return empty;
    }
}
