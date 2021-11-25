package kitchenpos.order.ui.dto.response;

public class OrderLineItemResponseDto {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    private OrderLineItemResponseDto() {
    }

    public OrderLineItemResponseDto(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
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
