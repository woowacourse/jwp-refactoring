package kitchenpos.dto.table;

public class ChangeOrderTableOrderableRequest {
    private final long orderTableId;
    private final boolean orderable;

    private ChangeOrderTableOrderableRequest(final long orderTableId, final boolean orderable) {
        this.orderTableId = orderTableId;
        this.orderable = orderable;
    }

    public static ChangeOrderTableOrderableRequest of(final long orderTableId, final boolean orderable) {
        return new ChangeOrderTableOrderableRequest(orderTableId, orderable);
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public boolean isOrderable() {
        return orderable;
    }
}
