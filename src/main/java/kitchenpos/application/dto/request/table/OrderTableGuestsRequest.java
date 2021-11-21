package kitchenpos.application.dto.request.table;

import kitchenpos.domain.table.OrderTable;

public class OrderTableGuestsRequest {

    private int numberOfGuests;

    public OrderTableGuestsRequest() {
    }

    public OrderTableGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable toEntity() {
        return new OrderTable.OrderTableBuilder()
                .setNumberOfGuests(numberOfGuests)
                .build();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
