package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.OrderTableCommand;

public record OrderTableRequest(Long id) {

    public OrderTableCommand toCommand() {
        return new OrderTableCommand(id);
    }
}
