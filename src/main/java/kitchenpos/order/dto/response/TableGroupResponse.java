package kitchenpos.order.dto.response;

import java.util.List;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.table.dto.response.TableResponse;

public class TableGroupResponse {

    private final Long id;
    private final List<TableResponse> tableResponses;

    private TableGroupResponse(Long id, List<TableResponse> tableResponses) {
        this.id = id;
        this.tableResponses = tableResponses;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                TableResponse.from(tableGroup.getOrderTables())
        );
    }

    public Long getId() {
        return id;
    }

    public List<TableResponse> getTableResponses() {
        return tableResponses;
    }
}
