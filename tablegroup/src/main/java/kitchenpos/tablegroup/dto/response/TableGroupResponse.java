package kitchenpos.tablegroup.dto.response;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.response.TableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final List<TableResponse> tableResponses;

    private TableGroupResponse(Long id, List<TableResponse> tableResponses) {
        this.id = id;
        this.tableResponses = tableResponses;
    }

    public static TableGroupResponse from(TableGroup tableGroup, List<OrderTable> groupedTables) {
        return new TableGroupResponse(
                tableGroup.getId(),
                TableResponse.from(groupedTables)
        );
    }

    public Long getId() {
        return id;
    }

    public List<TableResponse> getTableResponses() {
        return tableResponses;
    }
}
