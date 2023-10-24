package kitchenpos.domain;

import kitchenpos.common.BaseCreateTimeEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public void initOrderTables(final List<OrderTable> orderTables) {
        validateTableCount(orderTables);
        for (final OrderTable orderTable : orderTables) {
            this.orderTables.add(orderTable);
            orderTable.groupBy(this);
        }
    }

    private void validateTableCount(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹화하려는 테이블은 2개 이상이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
