package kitchenpos.table.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableChangeEmptyRequest {

    private Boolean empty;

    @JsonCreator
    public OrderTableChangeEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
