package kitchenpos.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import kitchenpos.domain.OrderLineItem;

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

    public Long getMenuId() {
        return menuId;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(null, menuId, quantity);
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
