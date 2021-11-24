package kitchenpos.application.dto;

import kitchenpos.application.dto.response.OrdersResponseDto;
import kitchenpos.application.dto.response.OrdersStatusResponseDto;
import kitchenpos.domain.Orders;

public class OrdersDtoAssembler {

    private OrdersDtoAssembler() {
    }

    public static OrdersResponseDto ordersResponseDto(Orders orders) {
        return new OrdersResponseDto(orders.getId());
    }

    public static OrdersStatusResponseDto ordersStatusResponseDto(Orders orders) {
        return new OrdersStatusResponseDto(orders.getOrderTableId(), orders.getOrderStatus());
    }
}
