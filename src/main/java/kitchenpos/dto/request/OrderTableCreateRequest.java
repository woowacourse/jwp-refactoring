package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import kitchenpos.domain.Empty;
import kitchenpos.domain.GuestNumber;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableStatus;

public class OrderTableCreateRequest {

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    private OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toTable() {
        return new OrderTable(new TableStatus(new Empty(empty), new GuestNumber(numberOfGuests)));
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }
}
