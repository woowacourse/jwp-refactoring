package kitchenpos.dto.order.response;

public class OrderLineItemResponse {

    private Long seq;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long seq, final Long menuId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
