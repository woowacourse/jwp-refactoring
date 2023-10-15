package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ChangeEmptyTableRequest {

    private Boolean empty;

    @JsonCreator
    public ChangeEmptyTableRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
