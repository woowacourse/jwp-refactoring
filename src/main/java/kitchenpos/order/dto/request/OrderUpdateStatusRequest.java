package kitchenpos.order.dto.request;

import javax.validation.constraints.NotNull;

public class OrderUpdateStatusRequest {

    @NotNull(message = "테이블의 상태를 입력해 주세요")
    private String orderStatus;

    protected OrderUpdateStatusRequest() {
    }

    public OrderUpdateStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
