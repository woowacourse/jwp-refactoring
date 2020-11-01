package kitchenpos.order.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long tableId;
    private List<OrderLineItemDto> orderLineItemDtos;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long tableId, List<OrderLineItemDto> orderLineItemDtos) {
        this.tableId = tableId;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public Long getTableId() {
        return tableId;
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }
}
