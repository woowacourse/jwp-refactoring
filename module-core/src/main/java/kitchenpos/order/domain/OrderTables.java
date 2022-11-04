package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validateAllOrderTablesEmpty(orderTables);
        this.orderTables = orderTables;
    }

    private void validateAllOrderTablesEmpty(final List<OrderTable> orderTables) {
        if (isAnyOrderTableEmpty(orderTables)) {
            throw new IllegalArgumentException("비어있지 않은 주문 테이블이 존재합니다.");
        }
    }

    private boolean isAnyOrderTableEmpty(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(OrderTable::isNotEmpty);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
