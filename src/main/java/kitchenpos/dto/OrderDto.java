package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderDto {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItemDtos;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItemDtos;
    }

    public void setOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos) {
        this.orderLineItemDtos = orderLineItemDtos;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        OrderDto orderDto = (OrderDto) object;
        return Objects.equals(id, orderDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
