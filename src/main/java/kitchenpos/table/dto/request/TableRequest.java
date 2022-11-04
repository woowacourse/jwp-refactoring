package kitchenpos.table.dto.request;

import javax.validation.constraints.NotNull;

public class TableRequest {

    @NotNull
    private int numberOfGuests;

    @NotNull
    private boolean empty;

    public TableRequest() {
    }

    public TableRequest(int numberOfGuests, boolean empty) {
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
