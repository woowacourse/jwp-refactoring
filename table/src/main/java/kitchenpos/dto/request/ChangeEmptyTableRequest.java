package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;

public class ChangeEmptyTableRequest {

    @NotNull
    private Boolean empty;

    @JsonCreator
    public ChangeEmptyTableRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
