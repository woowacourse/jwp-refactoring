package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, null, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validate(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        addOrderTables(orderTables);
    }

    private void validate(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            validateEmpty(savedOrderTable);
            validateGroupedAlready(savedOrderTable);
        }
    }

    private void validateEmpty(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않습니다.");
        }
    }

    private void validateGroupedAlready(final OrderTable savedOrderTable) {
        if (savedOrderTable.hasTableGroup()) {
            throw new IllegalArgumentException("이미 테이블 그룹으로 등록되어 있습니다.");
        }
    }

    private void addOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(this);
            orderTable.changeEmpty(false);
        }
        this.orderTables = new OrderTables(orderTables);
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
}
