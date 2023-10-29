package kitchenpos.common.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private LocalDateTime createdTime;
    private String orderStatus;
    private List<OrderLineItemDtoInOrderResponse> orderLineItems;

    public OrderResponse() {
    }

    public OrderResponse(final Long id, final LocalDateTime createdTime, final String orderStatus, final List<OrderLineItemDtoInOrderResponse> orderLineItems) {
        this.id = id;
        this.createdTime = createdTime;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemDtoInOrderResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
