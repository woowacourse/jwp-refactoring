package kitchenpos.ui.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableRequestDto;
import kitchenpos.application.dto.response.OrderTableResponseDto;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;

public class OrderTableAssembler {

    private OrderTableAssembler() {
    }

    public static OrderTableRequestDto orderTableRequestDto(OrderTableRequest request) {
        return new OrderTableRequestDto(request.getNumberOfGuests(), request.getEmpty());
    }

    public static OrderTableResponse orderTableResponse(OrderTableResponseDto responseDto) {
        return new OrderTableResponse(responseDto.getId());
    }

    public static List<OrderTableResponse> orderTableResponses(
        List<OrderTableResponseDto> responsesDto
    ) {
        return responsesDto.stream()
            .map(OrderTableAssembler::orderTableResponse)
            .collect(toList());
    }
}
