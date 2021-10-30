package kitchenpos.dto;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantiy;

    public OrderLineItemRequest(Long menuId, Long quantiy) {
        this.menuId = menuId;
        this.quantiy = quantiy;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantiy() {
        return quantiy;
    }
}
