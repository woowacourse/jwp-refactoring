package kitchenpos.dto;

import com.sun.istack.NotNull;

public class OrderTableIdRequest {
    @NotNull
    private Long id;

    public OrderTableIdRequest() {
    }

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
