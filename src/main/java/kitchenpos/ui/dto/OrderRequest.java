package kitchenpos.ui.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {

    @NotNull
    private Long orderTableId;

    @NotNull
    private List<OrderLineItemsRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItemsRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemsRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
