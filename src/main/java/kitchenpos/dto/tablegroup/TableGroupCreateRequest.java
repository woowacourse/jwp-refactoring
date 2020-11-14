package kitchenpos.dto.tablegroup;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class TableGroupCreateRequest {
    @NotEmpty(message = "주문 테이블 목록은 반드시 존재해야 합니다!")
    private List<Long> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
