package kitchenpos.ui.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableIdResponse {

    private final long id;

    @JsonCreator
    public OrderTableIdResponse(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
