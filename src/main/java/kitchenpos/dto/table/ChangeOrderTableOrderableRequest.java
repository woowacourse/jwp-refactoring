package kitchenpos.dto.table;

public class ChangeOrderTableOrderableRequest {
    private boolean orderable;

    private ChangeOrderTableOrderableRequest(final boolean orderable) {
        this.orderable = orderable;
    }

    public ChangeOrderTableOrderableRequest() {
    }

    public static ChangeOrderTableOrderableRequest of(final boolean orderable) {
        return new ChangeOrderTableOrderableRequest(orderable);
    }

    public boolean isOrderable() {
        return orderable;
    }
}
