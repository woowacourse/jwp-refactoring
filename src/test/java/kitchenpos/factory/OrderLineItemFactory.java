package kitchenpos.factory;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFactory {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemFactory() {

    }

    private OrderLineItemFactory(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemFactory builder() {
        return new OrderLineItemFactory();
    }

    public static OrderLineItemFactory copy(OrderLineItem orderLineItem) {
        return new OrderLineItemFactory(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public OrderLineItemFactory seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemFactory orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemFactory menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemFactory quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
