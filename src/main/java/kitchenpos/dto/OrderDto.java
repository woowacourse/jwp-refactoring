package kitchenpos.dto;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Order;

public class OrderDto {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItemDtos;

    public static OrderDto from(Order entity) {
        List<OrderLineItemDto> orderLineItemDtos = entity.getOrderLineItems()
                                                         .stream()
                                                         .map(OrderLineItemDto::from)
                                                         .collect(toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setId(entity.getId());
        orderDto.setOrderTableId(entity.getOrderTable().getId());
        orderDto.setOrderStatus(entity.getOrderStatus().name());
        orderDto.setOrderedTime(entity.getOrderedTime());
        orderDto.setOrderLineItemDtos(orderLineItemDtos);
        return orderDto;
    }



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

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }

    public void setOrderLineItemDtos(final List<OrderLineItemDto> orderLineItemDtos) {
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
