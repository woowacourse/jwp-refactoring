package kitchenpos.tablegroup.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                tableGroup.getGroupTables().toList().stream().map(OrderTable::getId).collect(Collectors.toList())
        );
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
