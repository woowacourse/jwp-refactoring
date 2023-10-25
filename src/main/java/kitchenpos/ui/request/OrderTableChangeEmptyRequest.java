package kitchenpos.ui.request;

import javax.validation.constraints.NotNull;

public class OrderTableChangeEmptyRequest {

    @NotNull
    private final Boolean empty;

    public OrderTableChangeEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
