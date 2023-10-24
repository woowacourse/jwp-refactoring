package kitchenpos.domain;

import java.util.Objects;

public class OrderLineItem {

    private Long seq;
    private Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItem(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public void orderedBy(final Long orderId) {
        this.orderId = orderId;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderLineItem orderLineItem = (OrderLineItem) o;
        if (Objects.isNull(this.seq) || Objects.isNull(orderLineItem.seq)) {
            return false;
        }
        return Objects.equals(seq, orderLineItem.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
