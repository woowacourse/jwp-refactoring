package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    @JsonCreator
    public ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
