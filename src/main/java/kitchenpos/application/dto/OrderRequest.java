package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import kitchenpos.domain.Order;

public class OrderRequest {

    private final Long orderTableId;
    @NotEmpty
    private final List<OrderLineItemRequest> orderLineItemRequests;

    private OrderRequest() {
        this(null, null);
    }

    public OrderRequest(
            Long orderTableId,
            List<OrderLineItemRequest> orderLineItemRequests
    ) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Order toOrder(String name, LocalDateTime now) {
        return null;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
