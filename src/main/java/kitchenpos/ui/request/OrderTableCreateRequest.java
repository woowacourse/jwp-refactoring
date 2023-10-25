package kitchenpos.ui.request;

import javax.validation.constraints.NotNull;

public class OrderTableCreateRequest {

    @NotNull
    private Integer numberOfGuests;
    @NotNull
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }

}
