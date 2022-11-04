package kitchenpos.dto;

import java.util.List;

public class OrderInfoSearchResponse {

    private List<OrderInfoResponse> orderInfo;

    public OrderInfoSearchResponse(List<OrderInfoResponse> orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<OrderInfoResponse> getOrderInfo() {
        return orderInfo;
    }
}
