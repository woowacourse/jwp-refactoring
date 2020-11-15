package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableStatusRequest {
    private boolean empty;

    @JsonCreator
    public OrderTableStatusRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
