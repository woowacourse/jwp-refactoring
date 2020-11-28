package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.model.Order;
import kitchenpos.order.model.OrderStatus;
import kitchenpos.orderline.application.dto.OrderLineCreateRequestDto;

public class OrderCreateRequestDto {
    private Long orderTableId;
    private List<OrderLineCreateRequestDto> orderLineCreateRequests;

    private OrderCreateRequestDto() {
    }

    public OrderCreateRequestDto(Long orderTableId,
        List<OrderLineCreateRequestDto> orderLineCreateRequests) {
        this.orderTableId = orderTableId;
        this.orderLineCreateRequests = orderLineCreateRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineCreateRequestDto> getOrderLineCreateRequests() {
        return orderLineCreateRequests;
    }

    public Order toEntity() {
        return new Order(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now());
    }
}
