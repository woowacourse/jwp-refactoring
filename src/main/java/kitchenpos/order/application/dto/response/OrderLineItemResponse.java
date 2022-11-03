package kitchenpos.order.application.dto.response;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long menuHistoryId;
    private final long quantity;

    public OrderLineItemResponse(final Long seq, final Long menuHistoryId, final long quantity) {
        this.seq = seq;
        this.menuHistoryId = menuHistoryId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuHistoryId;
    }

    public long getQuantity() {
        return quantity;
    }
}
