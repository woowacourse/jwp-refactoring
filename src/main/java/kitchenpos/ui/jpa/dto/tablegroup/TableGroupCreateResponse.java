package kitchenpos.ui.jpa.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.entity.OrderTable;

public class TableGroupCreateResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupCreateResponse() {
    }

    public TableGroupCreateResponse(Long id, LocalDateTime createdDate,
                                    List<OrderTable> orderTables) {
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
