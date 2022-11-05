package kitchenpos.table.application.dto.request;

import kitchenpos.table.domain.OrderTable;

public record OrderTableCreateCommand(int numberOfGuests, boolean empty) {

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }
}
