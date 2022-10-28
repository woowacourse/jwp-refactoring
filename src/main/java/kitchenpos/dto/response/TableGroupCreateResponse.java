package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupCreateResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    private TableGroupCreateResponse() {
    }

    public TableGroupCreateResponse(final Long id, final LocalDateTime createdDate,
                                    final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
