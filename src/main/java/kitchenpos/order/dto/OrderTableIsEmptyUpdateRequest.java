package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableIsEmptyUpdateRequest {

    @JsonProperty("empty")
    private boolean isEmpty;

    public OrderTableIsEmptyUpdateRequest() {
    }

    public OrderTableIsEmptyUpdateRequest(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
