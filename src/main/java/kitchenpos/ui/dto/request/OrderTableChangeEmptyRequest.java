package kitchenpos.ui.dto.request;

public class OrderTableChangeEmptyRequest {

    private Boolean empty;

    private OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
