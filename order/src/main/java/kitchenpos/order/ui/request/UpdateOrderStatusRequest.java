package kitchenpos.order.ui.request;

import java.util.Objects;

public class UpdateOrderStatusRequest {
    private final String orderStatus;

    public UpdateOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UpdateOrderStatusRequest that = (UpdateOrderStatusRequest) o;
        return Objects.equals(orderStatus, that.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus);
    }

    @Override
    public String toString() {
        return "UpdateOrderStatusRequest{" +
                "orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
