package kitchenpos.order.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    @JsonProperty
    private final Long seq;
    @JsonProperty
    private final Long menuId;
    @JsonProperty
    private final long quantity;

    public OrderLineItemResponse(final Long seq, final Long menuId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> from(final List<OrderLineItem> orderLineItems) {
        return orderLineItems
                .stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }
}
