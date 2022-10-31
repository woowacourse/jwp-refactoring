package kitchenpos.table.presentation.dto;

public class OrderTableChangeEmptyRequest {

    private Boolean empty;

    public OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
