package kitchenpos.dto.request;

import javax.validation.constraints.Min;

public class TableRequest {

    @Min(1)
    private final int numberOfGuests;

    private final boolean empty;

    public TableRequest(final int numberOfGuests, final boolean empty) {
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
