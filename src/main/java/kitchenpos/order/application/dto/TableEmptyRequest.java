package kitchenpos.order.application.dto;

public class TableEmptyRequest {

    private Boolean empty;

    public TableEmptyRequest() {
    }

    public TableEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
