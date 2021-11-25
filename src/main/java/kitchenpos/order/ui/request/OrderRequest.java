package kitchenpos.order.ui.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull(message = "주문 테이블은 null 일 수 없습니다.")
    private Long orderTable;

    @NotEmpty(message = "주문 항목은 최소 1개 이상이어야 합니다.")
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTable, List<OrderLineItemRequest> orderLineItems) {
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
