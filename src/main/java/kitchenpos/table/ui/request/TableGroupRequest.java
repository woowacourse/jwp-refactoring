package kitchenpos.table.ui.request;

import java.util.List;
import javax.validation.constraints.Size;

public class TableGroupRequest {

    @Size(min = 2, message = "주문 테이블은 최소 2개 이상이어야 합니다.")
    private List<Long> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
