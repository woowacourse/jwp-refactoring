package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    private static final int MINIMUM_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            addOrderTable(orderTable);
        }
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateEmptyOrderTable(orderTable);
            orderTable.validateNoTableGroup();
        }
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블은 비어있어야 합니다.");
        }
    }

    public void addOrderTable(final OrderTable orderTable) {
        orderTable.setTableGroup(this);
        this.orderTables.add(orderTable);
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
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
