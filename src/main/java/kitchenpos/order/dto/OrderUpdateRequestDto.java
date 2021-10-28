package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

public class OrderUpdateRequestDto {
    private Long id;
    private String orderStatus;

    public OrderUpdateRequestDto() {
    }

    public OrderUpdateRequestDto(Order order) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus();
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
