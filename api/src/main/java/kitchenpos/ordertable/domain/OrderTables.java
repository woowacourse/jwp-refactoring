package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void validateTableStatus() {
        for (OrderTable orderTable : this.orderTables) {
            if (!orderTable.isEmpty() || orderTable.getTableGroupId() != null) {
                throw new IllegalArgumentException("비어 있지 않거나, 이미 그룹으로 지정된 테이블은 단체로 지정할 수 없습니다.");
            }
        }
    }

    public void group(Long id) {
        for (OrderTable orderTable : this.orderTables) {
            orderTable.group(id);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

}
