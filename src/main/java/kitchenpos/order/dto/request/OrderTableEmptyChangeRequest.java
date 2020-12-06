package kitchenpos.order.dto.request;

import javax.validation.constraints.NotNull;

public class OrderTableEmptyChangeRequest {
    @NotNull
    private Boolean empty;

    public OrderTableEmptyChangeRequest() {
    }

    public OrderTableEmptyChangeRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
