package kitchenpos.order.application.dto;

import java.util.Objects;
import kitchenpos.order.domain.OrderStatus;

public class OrderStatusChangeRequest {

    private OrderStatus orderStatus;

    private OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderStatusChangeRequest that = (OrderStatusChangeRequest) o;
        return orderStatus == that.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus);
    }
}
