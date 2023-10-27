package kitchenpos.table.dto.request;

import javax.validation.constraints.Min;

public class TableCreateRequest {

    @Min(1)
    private final int numberOfGuests;

    private final boolean empty;

    public TableCreateRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
