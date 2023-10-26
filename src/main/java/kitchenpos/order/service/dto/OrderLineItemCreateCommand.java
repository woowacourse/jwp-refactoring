package kitchenpos.order.service.dto;

public class OrderLineItemCreateCommand {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemCreateCommand(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long menuId() {
        return menuId;
    }

    public Long quantity() {
        return quantity;
    }
}
