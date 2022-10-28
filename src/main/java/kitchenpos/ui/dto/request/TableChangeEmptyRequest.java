package kitchenpos.ui.dto.request;

public class TableChangeEmptyRequest {

    private Boolean empty;

    public TableChangeEmptyRequest() {}

    public TableChangeEmptyRequest(final Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
