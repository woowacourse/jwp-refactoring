package kitchenpos.order.dto.request;

import java.util.List;
import kitchenpos.order.dto.OrderLineItemsDto;

public class CreateOrderRequest {

    private Long  orderTableId;
    private List<OrderLineItemsDto> orderLineItems;

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemsDto> getOrderLineItems() {
        return orderLineItems;
    }
}
