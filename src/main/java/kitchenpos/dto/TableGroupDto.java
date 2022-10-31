package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupDto {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupDto() {
    }

    public TableGroupDto(final TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
        this.orderTables = tableGroup.getOrderTables();
    }

    public TableGroup toEntity() {
        return new TableGroup(id, createdDate, orderTables);
    }

    public void updateCreatedDateToNow() {
        createdDate = LocalDateTime.now();
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
