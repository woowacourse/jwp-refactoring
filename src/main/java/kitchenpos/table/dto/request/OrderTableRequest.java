package kitchenpos.table.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;

public class OrderTableRequest {

    @NotNull
    private Long id;

    @JsonCreator
    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
