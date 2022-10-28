package kitchenpos.domain;

import java.util.Objects;

@Deprecated
public class OrderLineItem {
    private Long id;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem(Long id, Long orderId, Long menuId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, null, menuId, quantity);
    }

    public Long getId() {
        return id;
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

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(orderId, that.orderId) && Objects.equals(
                menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, menuId, quantity);
    }
}
