package kitchenpos.dto;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private Long quantity;

    public OrderLineItemCreateRequest() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
