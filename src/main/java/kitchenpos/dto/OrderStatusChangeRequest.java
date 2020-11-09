package kitchenpos.dto;

import javax.validation.constraints.NotBlank;

public class OrderStatusChangeRequest {
    @NotBlank(message = "주문 상태가 반드시 존재해야 합니다!")
    private String orderStatus;

    public OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
