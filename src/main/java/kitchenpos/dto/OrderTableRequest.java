package kitchenpos.dto;

import com.sun.istack.NotNull;

public class OrderTableRequest {
    @NotNull
    private int numberOfGuests;

    @NotNull
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
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
