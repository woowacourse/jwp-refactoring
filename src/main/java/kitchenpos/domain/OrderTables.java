package kitchenpos.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTableStatus(orderTables);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty()) {
            throw new IllegalArgumentException("테이블 그룹은 비어 있을 수 없습니다.");
        }
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }
    }

    private void validateOrderTableStatus(final List<OrderTable> orderTables) {
        if (orderTables.stream()
            .anyMatch(OrderTable::isNotEmpty)) {
            throw new IllegalArgumentException("비어 있지 않은 테이블은 테이블 그룹으로 지정할 수 없습니다.");
        }
        if (orderTables.stream()
            .anyMatch(OrderTable::hasTableGroup)) {
            throw new IllegalArgumentException("이미 테이블 그룹으로 지정된 테이블은 테이블 그룹으로 지정할 수 없습니다.");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void registerTableGroup(final TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.registerTableGroup(tableGroup));
    }

    public void ungroup() {
        if (orderTables.stream()
            .anyMatch(OrderTable::hasProceedingOrder)) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 테이블 그룹을 해제할 수 없습니다.");
        }

        orderTables.forEach(OrderTable::ungroup);
    }
}
