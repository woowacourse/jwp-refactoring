package kitchenpos.ui.request;

import java.util.List;
import javax.validation.constraints.NotNull;

public class OrderCreateRequest {

    @NotNull
    @NotNull
    private Long orderTableId;

    @NotNull
    private List<OrderLineItemCreateRequest> orderLineItemCreateRequests;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(
            Long orderTableId,
            List<OrderLineItemCreateRequest> orderLineItemCreateRequests
    ) {
        this.orderTableId = orderTableId;
        this.orderLineItemCreateRequests = orderLineItemCreateRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItemCreateRequests() {
        return orderLineItemCreateRequests;
    }

}
