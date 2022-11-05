package kitchenpos.table.presentation.dto;

import kitchenpos.table.application.dto.request.OrderTableCommand;

public record OrderTableRequest(Long id) {

    public OrderTableCommand toCommand() {
        return new OrderTableCommand(id);
    }
}
