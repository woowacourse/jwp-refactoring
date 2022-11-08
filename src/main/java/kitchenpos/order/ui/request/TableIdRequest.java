package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableIdRequest {

    private final Long id;

    @JsonCreator
    public TableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
