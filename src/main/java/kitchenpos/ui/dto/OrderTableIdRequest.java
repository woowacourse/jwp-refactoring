package kitchenpos.ui.dto;

import javax.validation.constraints.NotNull;

public class OrderTableIdRequest {

    @NotNull
    private Long id;

    public OrderTableIdRequest() {
    }

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
