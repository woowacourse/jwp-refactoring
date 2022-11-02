package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import kitchenpos.domain.order.OrderLineItem;

public class OrderCreateRequest {

    @NotNull
    @JsonProperty(value = "orderTableId")
    private Long tableId;

    @NotNull
    private List<OrderLineItemCreateRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long tableId,
                              final List<OrderLineItemCreateRequest> orderLineItems) {
        this.tableId = tableId;
        this.orderLineItems = orderLineItems;
    }

//    public Order toOrder(final Long orderTableId, final LocalDateTime orderedTime) {
//        return new Order(orderTableId, OrderStatus.COOKING, orderedTime, toOrderLineItems());
//    }

    private List<OrderLineItem> toOrderLineItems() {
        return this.orderLineItems
                .stream()
                .map(OrderLineItemCreateRequest::toOrderLineItem)
                .collect(Collectors.toList());
    }

    public Long getTableId() {
        return tableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
