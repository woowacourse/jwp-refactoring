package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
        group(orderTables);
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void group(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(this);
            orderTable.updateEmpty(false);
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

    public void ungroup() {
        validateUngroupAvailable();
        for (OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(null);
            orderTable.updateEmpty(false);
        }
    }

    private void validateUngroupAvailable() {
        for (OrderTable orderTable : orderTables) {
            if(!orderTable.isOrderCompleted()) {
                throw new IllegalArgumentException("계산 완료되지 않은 테이블이 남아있어 단체 지정 해제가 불가능합니다.");
            }
        }
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
