package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.exception.InvalidTableGroupException;

@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        validateOrderTableSize(orderTables);
        validateTableUsing(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateOrderTableSize(final OrderTables orderTables) {
        if (orderTables.isEmpty() || orderTables.isSmallerThan(2)) {
            throw new InvalidTableGroupException("올바르지 않은 주문 테이블입니다.");
        }
    }

    private void validateTableUsing(final OrderTables orderTables) {
        if (orderTables.anyUsing()) {
            throw new InvalidTableGroupException("주문 테이블이 이미 사용 중입니다.");
        }
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, new OrderTables(Collections.emptyList()));
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, new OrderTables(orderTables));
    }

    protected TableGroup() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getValues();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TableGroup tableGroup)) {
            return false;
        }
        return Objects.equals(id, tableGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
