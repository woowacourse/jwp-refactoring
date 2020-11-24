package kitchenpos.table.dto;

import javax.validation.constraints.NotNull;

public class TableEmptyEditRequest {

    @NotNull
    private Boolean empty;

    public TableEmptyEditRequest() {
    }

    public TableEmptyEditRequest(@NotNull Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }

}
