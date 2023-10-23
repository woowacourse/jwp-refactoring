package kitchenpos.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull(message = "테이블 Id를 입력해 주세요.")
    private final Long orderTableId;

    @NotNull(message = "테이블의 상태를 입력해 주세요")
    private final String orderStatus;

    @NotNull(message = "주문 항목을 입력해 주세요")
    private final List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(
            final Long orderTableId,
            final String orderStatus,
            final List<OrderLineItemRequest> orderLineItemRequests
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
