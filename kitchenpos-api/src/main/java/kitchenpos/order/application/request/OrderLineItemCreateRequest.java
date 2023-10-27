package kitchenpos.order.application.request;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private Integer quantity;

    public OrderLineItemCreateRequest(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
