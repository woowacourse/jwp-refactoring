package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<TableResponse> orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<TableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                toTableResponses(tableGroup.getOrderTables()));
    }

    private static List<TableResponse> toTableResponses(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableResponse> getOrderTables() {
        return orderTables;
    }
}
