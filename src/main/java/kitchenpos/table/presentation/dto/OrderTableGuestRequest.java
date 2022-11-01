package kitchenpos.table.presentation.dto;

import kitchenpos.table.application.dto.request.OrderTableGuestCommand;

public record OrderTableGuestRequest(int numberOfGuests) {

    public OrderTableGuestCommand toCommand() {
        return new OrderTableGuestCommand(numberOfGuests);
    }
}
