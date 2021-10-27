package kitchenpos.ui.dto.response.order;

import java.time.LocalDateTime;
import kitchenpos.application.dto.response.order.OrderResponseDto;
import kitchenpos.ui.dto.response.table.OrderTableResponse;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTableResponse;
    private String orderStatus;
    private LocalDateTime orderedTime;

    private OrderResponse() {
    }

    public OrderResponse(Long id, OrderTableResponse orderTableResponse, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableResponse = orderTableResponse;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse from(OrderResponseDto orderResponseDto) {
        return new OrderResponse(orderResponseDto.getId(), OrderTableResponse.from(orderResponseDto.getOrderTableResponseDto()),
            orderResponseDto.getOrderStatus(), orderResponseDto.getOrderedTime());
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
