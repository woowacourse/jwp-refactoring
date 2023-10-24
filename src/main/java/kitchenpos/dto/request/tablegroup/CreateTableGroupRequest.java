package kitchenpos.dto.request.tablegroup;

import java.util.List;

public class CreateTableGroupRequest {

    private Long id;
    private List<OrderTableRequest> orderTables;

    public Long getId() {
        return id;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
