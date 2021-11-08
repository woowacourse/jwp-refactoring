package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    public TableGroupRequest(LocalDateTime createdDate, List<Long> orderTableIds) {
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
