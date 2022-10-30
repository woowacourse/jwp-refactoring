package kitchenpos.order.application.dto;

public class OrderLineItemsResponse {

    private Long orderId;
    private Long menuId;
    private Long quantity;

    public OrderLineItemsResponse(final Long orderId,
                                  final Long menuId,
                                  final Long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
