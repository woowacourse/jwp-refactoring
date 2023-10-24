package kitchenpos.dto.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.table.OrderTable;

public class OrderTableCreateRequest {

    private int numberOfGuests;

    @JsonProperty("empty")
    private boolean isEmpty;

    public OrderTableCreateRequest() {
    }

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

    public OrderTable toOrderTableWithoutGroup() {
        return OrderTable.of(numberOfGuests, isEmpty);
    }
}
