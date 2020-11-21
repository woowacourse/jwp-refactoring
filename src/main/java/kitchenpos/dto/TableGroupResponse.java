package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.GroupTables;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createDate;
    private List<TableResponse> orderTables;

    private TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createDate, List<TableResponse> orderTables) {
        this.id = id;
        this.createDate = createDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup, GroupTables tables) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
            TableResponse.ofList(tables.getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public List<TableResponse> getOrderTables() {
        return orderTables;
    }
}
