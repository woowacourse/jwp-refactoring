package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    public ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusDto toChangeOrderStatusDto() {
        return new ChangeOrderStatusDto(OrderStatus.valueOf(orderStatus));
    }
}
