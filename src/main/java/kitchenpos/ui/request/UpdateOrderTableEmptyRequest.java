package kitchenpos.ui.request;

public class UpdateOrderTableEmptyRequest {

    private Boolean empty;

    public UpdateOrderTableEmptyRequest() {
    }

    public UpdateOrderTableEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
