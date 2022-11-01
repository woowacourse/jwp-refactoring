package kitchenpos.table.presentation.dto;

import kitchenpos.table.application.dto.request.OrderTableCreateCommand;

public record OrderTableCreateRequest(int numberOfGuests, boolean empty) {

    public OrderTableCreateCommand toCommand() {
        return new OrderTableCreateCommand(numberOfGuests, empty);
    }
}
