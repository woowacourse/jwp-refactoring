package kitchenpos.dto.tablegroup;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

public class TableGroupCreateRequest {
    @NotEmpty(message = "주문 테이블 목록은 반드시 존재해야 합니다!")
    private List<Long> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroup toTableGroup(LocalDateTime localDateTime, List<OrderTable> orderTables) {
        return new TableGroup(localDateTime, orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
