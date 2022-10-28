package kitchenpos.dto;

import com.sun.istack.NotNull;

public class OrderTableRequest {
    @NotNull
    private Long id;

    public OrderTableRequest() {
    }

    public OrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
