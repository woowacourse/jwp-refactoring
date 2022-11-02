package kitchenpos.ui.jpa.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.entity.OrderTable;

public class TableGroupCreateResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    public TableGroupCreateResponse() {
    }

    public TableGroupCreateResponse(Long id, LocalDateTime createdDate,
                                    List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
