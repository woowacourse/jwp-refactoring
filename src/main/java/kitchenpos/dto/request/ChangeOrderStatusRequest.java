package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;

public class ChangeOrderStatusRequest {

    @NotNull
    private String orderStatus;

    @JsonCreator
    public ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
