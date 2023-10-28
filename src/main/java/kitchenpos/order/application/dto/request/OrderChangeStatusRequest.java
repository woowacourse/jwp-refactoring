package kitchenpos.order.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.order.OrderStatus;

import java.util.Objects;

public class OrderChangeStatusRequest {

    private final OrderStatus orderStatus;

    @JsonCreator
    public OrderChangeStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderChangeStatusRequest)) return false;
        OrderChangeStatusRequest that = (OrderChangeStatusRequest) o;
        return orderStatus == that.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus);
    }
}
