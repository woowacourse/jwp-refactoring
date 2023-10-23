package kitchenpos.ui.request;

import kitchenpos.domain.OrderTable;

public class TableCreateRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    public TableCreateRequest(final Integer numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public TableCreateRequest() {
    }

    public OrderTable toOrderTable() {
        return new OrderTable(
                null,
                null,
                numberOfGuests,
                empty
        );
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
