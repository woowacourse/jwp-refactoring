package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderStatusChangeRequest {

    private String status;

    @JsonCreator
    public OrderStatusChangeRequest(@JsonProperty("orderStatus") final String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
