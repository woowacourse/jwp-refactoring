package kitchenpos.table.dto.request;

import javax.validation.constraints.NotNull;

public class TableEmptyUpdateRequest {

    @NotNull
    private final Boolean empty;

    private TableEmptyUpdateRequest() {
        this(null);
    }

    public TableEmptyUpdateRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
