package kitchenpos.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class OrderLineItemCreateRequest {

    private final Long menuId;
    private final long quantity;

    @JsonCreator
    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
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
        if (!(o instanceof OrderLineItemCreateRequest)) return false;
        OrderLineItemCreateRequest that = (OrderLineItemCreateRequest) o;
        return quantity == that.quantity && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, quantity);
    }
}
