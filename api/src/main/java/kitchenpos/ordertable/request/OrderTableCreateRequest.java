package kitchenpos.ordertable.request;

import javax.validation.constraints.NotNull;

public class OrderTableCreateRequest {

    @NotNull
    private final int numberOfGuest;
    @NotNull
    private final boolean empty;

    public OrderTableCreateRequest(int numberOfGuest, boolean empty) {
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }

    public boolean isEmpty() {
        return empty;
    }
}
