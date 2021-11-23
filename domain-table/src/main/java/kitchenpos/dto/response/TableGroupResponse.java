package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<TableInGroupResponse> orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<TableInGroupResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponse(tableGroup));
    }

    private static List<TableInGroupResponse> orderTableResponse(TableGroup tableGroup) {
        return tableGroup.getOrderTableLists().stream()
                         .map(TableInGroupResponse::from)
                         .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableInGroupResponse> getOrderTables() {
        return orderTables;
    }
}
