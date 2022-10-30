package kitchenpos.dto.request;

public class OrderTableEmptyUpdateRequest {

    private Boolean empty;

    public OrderTableEmptyUpdateRequest(Boolean empty) {
        this.empty = empty;
    }

    private OrderTableEmptyUpdateRequest() {
    }

    public Boolean getEmpty() {
        return empty;
    }
}
