package kitchenpos.application.dto.request;

import java.util.List;

public class OrderCreateRequestDto {

    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequestDto> orderLineItemRequestDtos;

    public OrderCreateRequestDto(Long orderTableId, String orderStatus,
        List<OrderLineItemRequestDto> orderLineItemRequestDtos) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequestDtos = orderLineItemRequestDtos;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequestDto> getOrderLineItemRequestDtos() {
        return orderLineItemRequestDtos;
    }
}
