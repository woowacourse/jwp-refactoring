package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.OneToMany;
import kitchenpos.domain.OrderTable;

public class TableGroupRequest {

    private LocalDateTime createdDate;

    private List<Long> orderTables;

    public TableGroupRequest(LocalDateTime createdDate, List<Long> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<Long> orderTables) {
        this.orderTables = orderTables;
    }
}
