package kitchenpos.application.dto.request.order;

import java.util.List;

public class OrderCreateRequestDto {

    private Long orderTableId;
    private List<OrderLineItemRequestDto> orderLineItemRequestDtos;

    public OrderCreateRequestDto(Long orderTableId, List<OrderLineItemRequestDto> orderLineItemRequestDtos) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequestDtos = orderLineItemRequestDtos;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequestDto> getOrderLineItemRequestDtos() {
        return orderLineItemRequestDtos;
    }
}
