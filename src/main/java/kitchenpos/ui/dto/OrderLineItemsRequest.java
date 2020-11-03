package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;

public class OrderLineItemsRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private Long quantity;

    private OrderLineItemsRequest() {
    }

    public OrderLineItemsRequest(long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItemsRequest that = (OrderLineItemsRequest) o;
        return Objects.equals(menuId, that.menuId) &&
            Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemsOfOrderRequest{" +
            "menuId=" + menuId +
            ", quantity=" + quantity +
            '}';
    }
}
