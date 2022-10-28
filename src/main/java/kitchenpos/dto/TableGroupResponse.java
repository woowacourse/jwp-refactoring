package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<TableResponse> orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<TableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), toTableResponses(orderTables));
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
