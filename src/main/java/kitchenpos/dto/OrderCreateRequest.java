package kitchenpos.dto;

import java.util.List;

public class OrderCreateRequest {

    private List<OrderMenuRequest> orderMenuRequests;

    private Long tableId;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(List<OrderMenuRequest> orderMenuRequests, Long tableId) {
        this.orderMenuRequests = orderMenuRequests;
        this.tableId = tableId;
    }

    public List<OrderMenuRequest> getOrderMenuRequests() {
        return orderMenuRequests;
    }

    public Long getTableId() {
        return tableId;
    }
}
