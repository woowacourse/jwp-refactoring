package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        group(orderTables);
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateOrderTables(final List<OrderTable> orderTables){
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 개수 2개 이상부터 단체 지정 가능합니다.");
        }
    }

    private void group(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.groupBy(this);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroupBy(this);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
