package kitchenpos.dto.ordertable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableChangeStatusRequest {

    private boolean empty;

    @JsonCreator
    public OrderTableChangeStatusRequest(@JsonProperty("empty") final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
