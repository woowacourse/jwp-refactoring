package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderChangeDto {

    private final String orderStatus;

    @JsonCreator
    public OrderChangeDto(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
