package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import kitchenpos.domain.table.Empty;
import kitchenpos.domain.table.GuestNumber;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableStatus;

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
