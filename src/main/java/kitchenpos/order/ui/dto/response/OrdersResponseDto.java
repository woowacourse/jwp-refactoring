package kitchenpos.order.ui.dto.response;

import java.util.List;

public class OrdersResponseDto {

    private List<OrderResponseDto> orders;

    private OrdersResponseDto() {
    }

    public OrdersResponseDto(List<OrderResponseDto> orders) {
        this.orders = orders;
    }

    public List<OrderResponseDto> getOrders() {
        return orders;
    }
}
