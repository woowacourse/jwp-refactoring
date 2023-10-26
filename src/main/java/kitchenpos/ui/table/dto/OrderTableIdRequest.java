package kitchenpos.ui.table.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableIdRequest {

    @JsonProperty("id")
    private Long id;

    public OrderTableIdRequest() {
    }

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }
}
