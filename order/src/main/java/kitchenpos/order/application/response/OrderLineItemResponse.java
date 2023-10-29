package kitchenpos.order.application.response;

import kitchenpos.order.domain.OrderLineItem;

import java.util.Objects;

public class OrderLineItemResponse {
    private final Long seq;
    private final OrderedMenuResponse menu;
    private final long quantity;

    public OrderLineItemResponse(final Long seq,
                                 final OrderedMenuResponse menu,
                                 final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                new OrderedMenuResponse(
                        orderLineItem.getOrderedMenu().getMenuId(),
                        orderLineItem.getOrderedMenu().getName(),
                        orderLineItem.getOrderedMenu().getPrice()
                ),
                orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public OrderedMenuResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItemResponse that = (OrderLineItemResponse) o;
        return quantity == that.quantity
                && Objects.equals(seq, that.seq)
                && Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{" +
                "seq=" + seq +
                ", menu=" + menu +
                ", quantity=" + quantity +
                '}';
    }
}
