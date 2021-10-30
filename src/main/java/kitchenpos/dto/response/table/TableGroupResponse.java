package kitchenpos.dto.response.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<TableInGroupResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<TableInGroupResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return new TableGroupResponse();
        }
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponse(tableGroup));
    }

    private static List<TableInGroupResponse> orderTableResponse(TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
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
