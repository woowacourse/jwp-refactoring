package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableRequest {

    private final Long id;

    @JsonCreator
    public OrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
