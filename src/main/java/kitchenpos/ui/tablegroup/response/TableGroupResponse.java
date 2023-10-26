package kitchenpos.ui.tablegroup.response;

import kitchenpos.domain.tablegroup.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;

import java.util.List;

public class TableGroupResponse {
    private Long id;
    private String createdDate;
    private List<OrderTable> orderTables;

    public TableGroupResponse(Long id, String createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate().toString(),
                tableGroup.getOrderTables()
        );
    }

    public Long getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
