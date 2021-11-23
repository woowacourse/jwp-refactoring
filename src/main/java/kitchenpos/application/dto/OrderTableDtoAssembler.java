package kitchenpos.application.dto;

import kitchenpos.application.dto.response.OrderTableResponseDto;
import kitchenpos.domain.OrderTable;

public class OrderTableDtoAssembler {

    private OrderTableDtoAssembler() {
    }

    public static OrderTableResponseDto orderTableResponseDto(OrderTable orderTable) {
        return new OrderTableResponseDto(orderTable.getId());
    }
}
