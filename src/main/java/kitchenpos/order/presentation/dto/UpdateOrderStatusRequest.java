package kitchenpos.order.presentation.dto;

import kitchenpos.order.application.dto.UpdateOrderStatusRequestDto;

public class UpdateOrderStatusRequest {
    private String orderStatus;

    public UpdateOrderStatusRequest() {
    }

    public UpdateOrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public UpdateOrderStatusRequestDto toServiceDto(){
        return new UpdateOrderStatusRequestDto(orderStatus);
    }
}
