package kitchenpos.application.request;

import lombok.Getter;

@Getter
public class OrderTableGroupCreateRequest {

    private Long id;

    private OrderTableGroupCreateRequest() {
    }

    public OrderTableGroupCreateRequest(final Long id) {
        this.id = id;
    }
}
