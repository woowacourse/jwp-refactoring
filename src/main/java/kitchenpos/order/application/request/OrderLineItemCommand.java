package kitchenpos.order.application.request;

public class OrderLineItemCommand {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemCommand(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
