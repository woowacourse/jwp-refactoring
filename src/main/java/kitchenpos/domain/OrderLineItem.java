package kitchenpos.domain;

import java.util.Objects;

public class OrderLineItem {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        validateMenuId(menuId);
        validateQuantity(quantity);
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity < 1L) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuId(Long menuId) {
        if (menuId == null) {
            throw new IllegalArgumentException();
        }
    }

    public OrderLineItem(Long orderId, Long menuId, long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItem changeOrderId(Long orderId) {
        return new OrderLineItem(this.seq, orderId, this.menuId, this.quantity);
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
}
