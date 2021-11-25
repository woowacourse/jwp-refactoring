package kitchenpos.order.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponseDto> orderLineItems;

    private OrderResponseDto() {
    }

    public OrderResponseDto(
        Long id,
        Long orderTableId,
        String orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineItemResponseDto> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
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

    public List<OrderLineItemResponseDto> getOrderLineItems() {
        return orderLineItems;
    }
}
