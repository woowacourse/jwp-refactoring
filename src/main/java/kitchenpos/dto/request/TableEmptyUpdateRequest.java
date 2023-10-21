package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class TableEmptyUpdateRequest {

    @NotNull
    private final Boolean empty;

    public TableEmptyUpdateRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
