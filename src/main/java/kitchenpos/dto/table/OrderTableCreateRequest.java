package kitchenpos.dto.table;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private final int numberOfGuests;
    private final boolean isEmpty;

    public OrderTableCreateRequest(int numberOfGuests, boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public OrderTable toEmptyOrderTable() {
        return new OrderTable(null, null, numberOfGuests, isEmpty);
    }
}
