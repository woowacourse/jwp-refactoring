package kitchenpos.order.dto.request;

import javax.validation.constraints.NotNull;

public class OrderTableRequest {

    @NotNull
    private final Long id;

    private OrderTableRequest() {
        this(null);
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
