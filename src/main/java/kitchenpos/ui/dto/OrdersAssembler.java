package kitchenpos.ui.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.request.OrderLineItemRequestDto;
import kitchenpos.application.dto.request.OrdersRequestDto;
import kitchenpos.application.dto.request.OrdersStatusRequestDto;
import kitchenpos.application.dto.response.OrdersResponseDto;
import kitchenpos.application.dto.response.OrdersStatusResponseDto;
import kitchenpos.ui.dto.request.OrdersRequest;
import kitchenpos.ui.dto.request.OrdersStatusRequest;
import kitchenpos.ui.dto.response.OrdersResponse;
import kitchenpos.ui.dto.response.OrdersStatusResponse;

public class OrdersAssembler {

    private OrdersAssembler() {
    }

    public static OrdersRequestDto ordersRequestDto(OrdersRequest request) {
        List<OrderLineItemRequestDto> orderLineItems = request.getOrderLineItems().stream()
            .map(source -> new OrderLineItemRequestDto(source.getMenuId(), source.getQuantity()))
            .collect(toList());

        return new OrdersRequestDto(request.getOrderTableId(), orderLineItems);
    }

    public static OrdersResponse ordersResponse(OrdersResponseDto responseDto) {
        return new OrdersResponse(responseDto.getId());
    }

    public static List<OrdersResponse> ordersResponses(List<OrdersResponseDto> responsesDto) {
        return responsesDto.stream()
            .map(OrdersAssembler::ordersResponse)
            .collect(toList());
    }

    public static OrdersStatusRequestDto ordersStatusRequestDto(
        Long orderId,
        OrdersStatusRequest request
    ) {
        return new OrdersStatusRequestDto(orderId, request.getOrderStatus());
    }

    public static OrdersStatusResponse ordersStatusResponse(OrdersStatusResponseDto responseDto) {
        return new OrdersStatusResponse(responseDto.getOrderStatus());
    }
}
