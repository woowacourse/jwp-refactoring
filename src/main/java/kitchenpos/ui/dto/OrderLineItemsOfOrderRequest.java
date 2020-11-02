package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;

public class OrderLineItemsOfOrderRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private Long quantity;

    private OrderLineItemsOfOrderRequest() {
    }

    public OrderLineItemsOfOrderRequest(long menuId, long quantity) {
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
        OrderLineItemsOfOrderRequest that = (OrderLineItemsOfOrderRequest) o;
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
