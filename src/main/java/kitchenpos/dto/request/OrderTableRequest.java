package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class OrderTableRequest {

    @NotNull
    private final Long id;

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
