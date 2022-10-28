package kitchenpos.dto.response;

public class OrderLineItemCreateResponse {

    private Long seq;
    private Long menuId;
    private long quantity;

    private OrderLineItemCreateResponse() {
    }

    public OrderLineItemCreateResponse(final Long seq, final Long menuId, final long quantity) {
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
