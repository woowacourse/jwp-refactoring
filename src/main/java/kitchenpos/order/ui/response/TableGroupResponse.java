package kitchenpos.order.ui.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private final long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableIdResponse> orderTables;

    @JsonCreator
    public TableGroupResponse(final long id, final LocalDateTime createdDate,
                              final List<OrderTableIdResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableIdResponse> getOrderTables() {
        return orderTables;
    }
}
