package kitchenpos.tablegroup.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableIdRequest {

    private Long id;

    @JsonCreator
    public OrderTableIdRequest(@JsonProperty("id") final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
