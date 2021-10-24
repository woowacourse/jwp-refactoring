package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;

public class OrderResponseDto {

    private Long id;
    private OrderTableResponseDto orderTableResponseDto;
    private String orderStatus;
    private LocalDateTime orderedTime;

    private OrderResponseDto() {
    }

    public OrderResponseDto(
        Long id,
        OrderTableResponseDto orderTableResponseDto,
        String orderStatus,
        LocalDateTime orderedTime
    ) {
        this.id = id;
        this.orderTableResponseDto = orderTableResponseDto;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(order.getId(), OrderTableResponseDto.from(order.getOrderTable()), order.getOrderStatus().name(), order.getOrderedTime());
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponseDto getOrderTableResponseDto() {
        return orderTableResponseDto;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
