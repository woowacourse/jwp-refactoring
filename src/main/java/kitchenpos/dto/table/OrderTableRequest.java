package kitchenpos.dto.table;

import kitchenpos.domain.order.OrderTable;

import javax.validation.constraints.NotNull;

public class OrderTableRequest {
    @NotNull(groups = {TableValidationGroup.create.class, TableValidationGroup.changeNumberOfGuests.class})
    private Integer numberOfGuests;

    @NotNull(groups = {TableValidationGroup.create.class, TableValidationGroup.changeEmpty.class})
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}


