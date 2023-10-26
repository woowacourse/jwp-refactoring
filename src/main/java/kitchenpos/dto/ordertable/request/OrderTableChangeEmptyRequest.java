package kitchenpos.dto.ordertable.request;

public class OrderTableChangeEmptyRequest {
    private final boolean isEmpty;

    public OrderTableChangeEmptyRequest(final boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
