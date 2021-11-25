package kitchenpos.order.ui.request;

import javax.validation.constraints.NotBlank;

public class OrderStatusRequest {

    @NotBlank(message = "주문 상태는 공백일 수 없습니다.")
    private String orderStatus;

    protected OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
