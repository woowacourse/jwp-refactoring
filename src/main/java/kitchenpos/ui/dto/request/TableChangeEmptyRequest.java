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

    @Override
    public String toString() {
        return "TableChangeEmptyRequest{" +
                "empty=" + empty +
                '}';
    }
}
