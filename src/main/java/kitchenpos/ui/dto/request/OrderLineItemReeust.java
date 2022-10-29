package kitchenpos.ui.dto.request;

public class OrderLineItemReeust {

    private Long menuId;
    private int quantity;

    private OrderLineItemReeust() {}

    public OrderLineItemReeust(final Long menuId, final int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItemReeust{" +
                "menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
