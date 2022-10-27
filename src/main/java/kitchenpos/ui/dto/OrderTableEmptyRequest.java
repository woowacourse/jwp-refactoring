package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.OrderTableEmptyCommand;

public record OrderTableEmptyRequest(boolean empty) {

    public OrderTableEmptyCommand toCommand() {
        return new OrderTableEmptyCommand(empty);
    }
}
