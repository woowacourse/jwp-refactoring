package kitchenpos.ui.dto;

public class OrderLineItemsOfOrderRequest {

    private long menuId;
    private long quantity;

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
    public String toString() {
        return "OrderLineItemsOfOrderRequest{" +
            "menuId=" + menuId +
            ", quantity=" + quantity +
            '}';
    }
}
