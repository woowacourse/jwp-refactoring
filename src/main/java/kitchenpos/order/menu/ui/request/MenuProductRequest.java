package kitchenpos.order.menu.ui.request;

import java.util.Objects;

public class MenuProductRequest {
    private final Long productId;
    private final long quantity;

    public MenuProductRequest(final Long productId,
                              final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProductRequest that = (MenuProductRequest) o;
        return quantity == that.quantity
                && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductRequest{" +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
