package kitchenpos.dto.request.order;

public class CreateOrderLineItemRequest {

    private Long menuId;
    private long quantity;

    private CreateOrderLineItemRequest() {
    }

    public CreateOrderLineItemRequest(Long menuId, long quantity) {
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
