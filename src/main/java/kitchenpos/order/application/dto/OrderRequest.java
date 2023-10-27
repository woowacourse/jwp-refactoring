package kitchenpos.order.application.dto;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private List<MenuQuantityDto> orderLineItems;

    private OrderRequest() {}

    public OrderRequest(final Long orderTableId, final List<MenuQuantityDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<MenuQuantityDto> getOrderLineItems() {
        return orderLineItems;
    }
}
