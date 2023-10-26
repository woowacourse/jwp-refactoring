package kitchenpos.tablegroup.vo;

import java.util.List;
import kitchenpos.order.domain.OrderTable;

public class GroupTableIds {

    private final List<Long> orderTableIds;

    public GroupTableIds(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public void validateExistenceAndDuplication(List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 있거나 주문 테이블이 중복되었습니다.");
        }
    }
}
