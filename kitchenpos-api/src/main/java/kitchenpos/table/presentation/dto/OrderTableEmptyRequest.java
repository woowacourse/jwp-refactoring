package kitchenpos.table.presentation.dto;

import kitchenpos.table.application.dto.request.OrderTableEmptyCommand;

public record OrderTableEmptyRequest(boolean empty) {

    public OrderTableEmptyCommand toCommand() {
        return new OrderTableEmptyCommand(empty);
    }
}
