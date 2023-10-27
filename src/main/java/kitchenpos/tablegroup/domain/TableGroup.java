package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static java.util.Objects.isNull;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(fetch = LAZY, mappedBy = "tableGroupId")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void ungroup(UngroupValidator ungroupValidator) {
        ungroupValidator.validate(this);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validate(final List<OrderTable> orderTables) {
        if (isNull(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }
        for (final OrderTable savedOrderTable : orderTables) {
            validate(savedOrderTable);
        }
    }

    private void validate(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블만 테이블 그룹으로 만들 수 있습니다.");
        }
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("다른 테이블 그룹에 포함되어있습니다. table id: " + savedOrderTable.getId());
        }
    }

    public void changeOrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.groupBy(id);
        }
        this.orderTables = orderTables;
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

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
