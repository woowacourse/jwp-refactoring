package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<TableResponse> orderTables;

    private TableGroupResponse() {
    }

    private TableGroupResponse(final Long id, final LocalDateTime createdDate,
                              final List<TableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), toTableResponses(tableGroup.getTables()));
    }

    public static List<TableResponse> toTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toUnmodifiableList());
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
