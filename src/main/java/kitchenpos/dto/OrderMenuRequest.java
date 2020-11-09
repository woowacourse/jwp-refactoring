package kitchenpos.dto;

public class OrderMenuRequest {

    private Long menuId;

    private Long quantity;

    public OrderMenuRequest() {
    }

    public OrderMenuRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
