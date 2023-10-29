package kitchenpos.order.application.dto;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemCreateResponse {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;
    private final String name;
    private final BigDecimal price;

    public OrderLineItemCreateResponse(final Long seq, final Long orderId, final Long menuId, final long quantity,
                                       final String name, final BigDecimal price) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public static OrderLineItemCreateResponse of(final OrderLineItem orderLineItem) {
        return new OrderLineItemCreateResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity(),
                orderLineItem.getName(),
                orderLineItem.getPrice().getValue()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
