package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.OrderTableCreateCommand;

public record OrderTableCreateRequest(int numberOfGuests, boolean empty) {

    public OrderTableCreateCommand toCommand() {
        return new OrderTableCreateCommand(numberOfGuests, empty);
    }
}
