package kitchenpos.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {

    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    @JsonCreator
    public TableGroupRequest(final LocalDateTime createdDate, final List<OrderTableRequest> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
