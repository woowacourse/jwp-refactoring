package kitchenpos.order.application.dto;

public class OrderLineItemCommand {

    private Long menuId;
    private long quantity;

    public OrderLineItemCommand(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long menuId() {
        return menuId;
    }

    public long quantity() {
        return quantity;
    }
}
