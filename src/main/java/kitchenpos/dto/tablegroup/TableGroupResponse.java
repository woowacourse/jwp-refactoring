package kitchenpos.dto.tablegroup;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableDto> orderTables;
    private LocalDateTime createdDate;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, List<OrderTableDto> orderTables, LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        List<OrderTableDto> orderTableDtos = tableGroup.getOrderTables().stream()
                .map(OrderTableDto::of)
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), orderTableDtos, tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
