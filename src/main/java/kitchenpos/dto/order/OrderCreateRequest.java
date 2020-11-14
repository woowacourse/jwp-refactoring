package kitchenpos.dto.order;

import kitchenpos.dto.orderlineitem.OrderLineItemCreateRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderCreateRequest {
    @NotNull(message = "주문 테이블은 반드시 존재해야 합니다!")
    private Long orderTableId;

    @NotEmpty
    private List<OrderLineItemCreateRequest> orderLineItemCreateRequests;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
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
