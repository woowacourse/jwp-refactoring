package kitchenpos.order.ui.request;

import java.util.Objects;

public class OrderLineItemRequest {
    private final Long menuId;
    private final long quantity;

    public OrderLineItemRequest(final Long menuId,
                                final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItemRequest that = (OrderLineItemRequest) o;
        return quantity == that.quantity && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemRequest{" +
                "menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
