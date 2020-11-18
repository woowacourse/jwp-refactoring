package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableId {
    @NotNull
    private final Long id;

    @JsonCreator
    public OrderTableId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
