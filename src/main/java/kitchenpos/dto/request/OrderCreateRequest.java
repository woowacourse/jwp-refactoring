package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {

    private final Long orderTableId;

    private final List<CreateOrderLineItemRequest> createOrderLineItemRequests;

    public OrderCreateRequest(Long orderTableId, List<CreateOrderLineItemRequest> createOrderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.createOrderLineItemRequests = createOrderLineItemRequests;
    }

    public List<Long> menuIds() {
        return createOrderLineItemRequests.stream()
                .map(CreateOrderLineItemRequest::menuId)
                .collect(Collectors.toList());
    }

    public Long orderTableId() {
        return orderTableId;
    }

    public List<CreateOrderLineItemRequest> createOrderLineItemRequests() {
        return createOrderLineItemRequests;
    }
}
