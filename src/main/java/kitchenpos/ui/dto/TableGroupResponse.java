package kitchenpos.ui.dto;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<TableResponse> tables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<TableResponse> tables) {
        this.id = id;
        this.createdDate = createdDate;
        this.tables = tables;
    }

    public static TableGroupResponse of(TableGroup newTableGroup, List<TableResponse> orderTables) {
        return new TableGroupResponse(newTableGroup.getId(), newTableGroup.getCreatedDate(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableResponse> getTables() {
        return tables;
    }
}
