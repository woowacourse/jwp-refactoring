package kitchenpos.order.dto.request;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public class OrderCreationRequest {

    @NotNull
    private final Long orderTableId;
    @NotNull
    private final List<OrderLineItemRequest> orderLineItemRequests;

    public OrderCreationRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Map<Long, Long> getQuantitiesByMenuId(){
        return orderLineItemRequests.stream()
                .collect(Collectors.toMap(OrderLineItemRequest::getMenuId, OrderLineItemRequest::getQuantity));
    }

}
