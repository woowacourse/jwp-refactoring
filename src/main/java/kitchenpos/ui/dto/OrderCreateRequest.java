package kitchenpos.ui.dto;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderCreateRequest {

    @NotNull
    private Long orderTableId;

    @NotEmpty
    private List<OrderLineItemsOfOrderRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId,
        List<OrderLineItemsOfOrderRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemsOfOrderRequest> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderCreateRequest that = (OrderCreateRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) &&
            Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItems);
    }

    @Override
    public String toString() {
        return "OrderCreateRequest{" +
            "orderTableId=" + orderTableId +
            ", orderLineItems=" + orderLineItems +
            '}';
    }
}
