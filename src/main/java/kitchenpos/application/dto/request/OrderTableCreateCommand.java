package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderTable;

public record OrderTableCreateCommand(int numberOfGuests, boolean empty) {

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }
}
