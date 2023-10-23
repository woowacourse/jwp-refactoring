package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {

    @NotNull(message = "테이블의 상태를 입력해 주세요")
    private final String orderStatus;

    public OrderStatusUpdateRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
