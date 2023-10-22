package kitchenpos.dto.table;

public class OrderTableIsEmptyUpdateRequest {

    private boolean isEmpty;

    public OrderTableIsEmptyUpdateRequest() {
    }

    public OrderTableIsEmptyUpdateRequest(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
