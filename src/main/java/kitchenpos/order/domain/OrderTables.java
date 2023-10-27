package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.table_group.domain.TableGroup;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validate(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty()) {
            throw new IllegalArgumentException("테이블 그룹은 비어 있을 수 없습니다.");
        }
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }
    }

    public void group(final TableGroup tableGroup) {
        validateOrderTableStatus(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(tableGroup.getId()));
    }

    private void validateOrderTableStatus(final List<OrderTable> orderTables) {
        if (orderTables.stream()
            .anyMatch(OrderTable::isEmpty)) {
            throw new IllegalArgumentException("비어 있지 않은 테이블은 테이블 그룹으로 지정할 수 없습니다.");
        }
        if (orderTables.stream()
            .anyMatch(OrderTable::hasTableGroup)) {
            throw new IllegalArgumentException("이미 테이블 그룹으로 지정된 테이블은 테이블 그룹으로 지정할 수 없습니다.");
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
