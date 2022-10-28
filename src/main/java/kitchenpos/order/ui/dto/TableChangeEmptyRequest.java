package kitchenpos.order.ui.dto;

import javax.validation.constraints.NotNull;

public class TableChangeEmptyRequest {

    @NotNull
    private Boolean empty;

    private TableChangeEmptyRequest() {
    }

    public TableChangeEmptyRequest(final Boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
