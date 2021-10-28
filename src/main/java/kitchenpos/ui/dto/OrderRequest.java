package kitchenpos.ui.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    @NotNull(message = "주문테이블 아이디가 null입니다.")
    private Long orderTableId;
    @NotEmpty(message = "주문 항목이 비어있습니다.")
    private List<OrderItemRequest> orderLineItems;

    private OrderRequest() {
    }

    private OrderRequest(Long orderTableId, List<OrderItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(Long orderTableId, List<OrderItemRequest> orderItemRequests) {
        return new OrderRequest(orderTableId, orderItemRequests);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
