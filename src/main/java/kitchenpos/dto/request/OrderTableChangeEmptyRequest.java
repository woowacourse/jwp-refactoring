package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class OrderTableChangeEmptyRequest {

    @NotNull
    private Boolean empty;

    private OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(final Boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
