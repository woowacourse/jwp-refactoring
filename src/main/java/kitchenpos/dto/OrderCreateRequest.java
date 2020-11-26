package kitchenpos.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderCreateRequest {

    @NotEmpty
    private List<OrderMenuRequest> orderMenuRequests;

    @NotNull
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
