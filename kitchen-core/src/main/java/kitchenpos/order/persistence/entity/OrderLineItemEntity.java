package kitchenpos.order.persistence.entity;

import kitchenpos.menu.domain.Price;
import kitchenpos.order.domain.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemEntity {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final String name;
    private final BigDecimal price;
    private final long quantity;


    public OrderLineItemEntity(final Long seq, final Long orderId, final Long menuId, final String name, final BigDecimal price, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemEntity of(final Long orderId, final OrderLineItem orderLineItem) {
        return new OrderLineItemEntity(orderLineItem.getSeq(), orderId,
                orderLineItem.getMenuId(), orderLineItem.getName(), orderLineItem.getPrice().getValue(), orderLineItem.getQuantity());
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(seq, menuId, name, new Price(price), quantity);
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

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
