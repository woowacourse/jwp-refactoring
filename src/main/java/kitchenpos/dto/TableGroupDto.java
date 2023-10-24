package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public static TableGroupDto from(TableGroup tableGroup) {
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables());
    }

    public TableGroupDto(LocalDateTime createdDate) {
        this(null, createdDate, null);
    }

    public TableGroupDto(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroupDto(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup toDomain() {
        return new TableGroup(id, createdDate, orderTables);
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
