package kitchenpos.order.ui.dto.order;

import java.time.LocalDateTime;
import java.util.List;

public class ChangeOrderStatusResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<Long> orderLineItemIds;

    public ChangeOrderStatusResponse() {
    }

    public ChangeOrderStatusResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                             List<Long> orderLineItemIds) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemIds = orderLineItemIds;
    }

    public Long getId() {
        return id;
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

    public List<Long> getOrderLineItems() {
        return orderLineItemIds;
    }
}
