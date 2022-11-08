package kitchenpos.table.ui.dto.request;

import javax.validation.constraints.NotNull;

public class OrderTableChangeEmptyRequest {

    @NotNull
    private Boolean empty;

    public OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(final Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
