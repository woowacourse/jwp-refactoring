package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableRequest {

    private Long id;

    @JsonCreator
    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
