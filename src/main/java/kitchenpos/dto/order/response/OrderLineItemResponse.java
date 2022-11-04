package kitchenpos.dto.order.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long seq, final Long orderId, final String menuName, final BigDecimal menuPrice,
                                 final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getName(),
                orderLineItem.getPrice().getValue(),
                orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> from(final OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems()
                .stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
