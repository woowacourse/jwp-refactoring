package kitchenpos.dto.table;

public class OrderTableIsEmptyUpdateRequest {

    private final boolean isEmpty;

    public OrderTableIsEmptyUpdateRequest(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
