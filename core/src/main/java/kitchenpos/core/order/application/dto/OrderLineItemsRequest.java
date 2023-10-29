package kitchenpos.core.order.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class OrderLineItemsRequest {
    @JsonProperty
    private final Long menuId;
    @JsonProperty
    private final Long quantity;

    @JsonCreator
    public OrderLineItemsRequest(final Long menuId, final Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderLineItemsRequest that = (OrderLineItemsRequest) o;
        return Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, quantity);
    }
}
