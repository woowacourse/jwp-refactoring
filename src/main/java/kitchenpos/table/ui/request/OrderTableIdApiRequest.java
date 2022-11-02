package kitchenpos.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import kitchenpos.table.application.request.OrderTableIdRequest;

public class OrderTableIdApiRequest {

    private final Long id;

    @JsonCreator
    public OrderTableIdApiRequest(Long id) {
        this.id = id;
    }

    public OrderTableIdRequest toServiceRequest() {
        return new OrderTableIdRequest(id);
    }
}
