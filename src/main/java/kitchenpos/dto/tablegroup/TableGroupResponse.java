package kitchenpos.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.table.TableResponse;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<TableResponse> tables;

    private TableGroupResponse() {
    }

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<TableResponse> tables) {
        this.id = id;
        this.createdDate = createdDate;
        this.tables = tables;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<Table> tables) {
        List<TableResponse> tableResponses = tables.stream()
            .map(TableResponse::of)
            .collect(Collectors.toList());

        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tableResponses);
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
