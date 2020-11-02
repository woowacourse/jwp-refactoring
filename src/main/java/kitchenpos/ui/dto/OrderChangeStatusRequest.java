package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;

public class OrderChangeStatusRequest {

    @NotEmpty
    private String orderStatus;

    private OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderChangeStatusRequest that = (OrderChangeStatusRequest) o;
        return Objects.equals(orderStatus, that.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus);
    }

    @Override
    public String toString() {
        return "OrderChangeStatusRequest{" +
            "orderStatus='" + orderStatus + '\'' +
            '}';
    }
}
