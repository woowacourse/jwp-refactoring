package kitchenpos.order.ui;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    public OrderRequest(Long orderTableId,
                        String orderStatus,
                        LocalDateTime orderedTime,
                        List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItems;
    }
}
