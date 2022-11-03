package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableEmptyStatusRequest {

    private boolean empty;

    public OrderTableEmptyStatusRequest() {
    }

    public OrderTableEmptyStatusRequest(final boolean empty) {
        this.empty = empty;
    }

    @JsonProperty("empty")
    public boolean isEmpty() {
        return empty;
    }
}
