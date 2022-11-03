package kitchenpos.order.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemCreateResponse {

    private Long seq;
    private Long orderId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    public OrderLineItemCreateResponse() {
    }

    public OrderLineItemCreateResponse(final Long seq, final Long orderId, final String menuName,
                                       final BigDecimal menuPrice, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static List<OrderLineItemCreateResponse> from(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemCreateResponse::from)
                .collect(Collectors.toList());
    }

    private static OrderLineItemCreateResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemCreateResponse(orderLineItem.getSeq(), orderLineItem.getOrder().getId(),
                orderLineItem.getOrderedMenu().getMenuName(), orderLineItem.getOrderedMenu().getMenuPrice(),
                orderLineItem.getQuantity());
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
