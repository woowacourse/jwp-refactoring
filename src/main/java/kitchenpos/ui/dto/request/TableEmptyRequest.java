package kitchenpos.ui.dto.request;

public class TableEmptyRequest {

    private Boolean empty;

    public TableEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
