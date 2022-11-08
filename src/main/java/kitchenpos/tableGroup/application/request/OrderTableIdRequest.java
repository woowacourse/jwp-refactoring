package kitchenpos.tableGroup.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableIdRequest {

    private final Long id;

    @JsonCreator
    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
