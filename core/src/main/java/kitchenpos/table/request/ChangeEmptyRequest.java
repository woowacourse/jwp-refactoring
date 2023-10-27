package kitchenpos.table.request;

public class ChangeEmptyRequest {

    private Boolean empty;

    public ChangeEmptyRequest() {
    }

    public ChangeEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
