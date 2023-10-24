package kitchenpos.dto.request;

public class CreateOrderLineItemRequest {

    private final Long menuId;
    private final Long quantity;

    public CreateOrderLineItemRequest(Long menuId, Long quantity) {
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
