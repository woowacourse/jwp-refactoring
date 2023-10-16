package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableEmptyStatusChangeRequest {

    private final Boolean empty;

    @JsonCreator
    public OrderTableEmptyStatusChangeRequest(final Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
