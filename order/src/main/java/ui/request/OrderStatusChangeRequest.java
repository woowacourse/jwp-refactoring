package ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotNull;

public class OrderStatusChangeRequest {

    @NotNull
    private final String orderStatus;

    @JsonCreator
    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
