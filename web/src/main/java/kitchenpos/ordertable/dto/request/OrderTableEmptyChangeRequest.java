package kitchenpos.ordertable.dto.request;

public class OrderTableEmptyChangeRequest {

    private final boolean isEmpty;

    public OrderTableEmptyChangeRequest(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
