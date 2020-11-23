package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> orderTables;

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블을 2개 이상 입력하세요");
        }
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블이 있습니다");
        }
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("이미 그룹화된 주문 테이블입니다.");
            }
        }

        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void groupTables(TableGroup savedTableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.groupBy(savedTableGroup);
        }
    }
}
