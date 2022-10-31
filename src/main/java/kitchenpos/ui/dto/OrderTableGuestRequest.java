package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.OrderTableGuestCommand;

public record OrderTableGuestRequest(int numberOfGuests) {

    public OrderTableGuestCommand toCommand() {
        return new OrderTableGuestCommand(numberOfGuests);
    }
}
