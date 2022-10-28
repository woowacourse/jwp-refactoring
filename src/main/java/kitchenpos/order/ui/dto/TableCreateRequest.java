package kitchenpos.order.ui.dto;

import javax.validation.constraints.NotNull;
import kitchenpos.order.domain.Empty;
import kitchenpos.order.domain.GuestNumber;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableStatus;

public class TableCreateRequest {

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    private TableCreateRequest() {
    }

    public TableCreateRequest(int numberOfGuests, boolean empty) {
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
