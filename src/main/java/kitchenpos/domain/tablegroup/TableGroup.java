package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.validator.OrderTableValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
        for (OrderTable orderTable : orderTables) {
            orderTable.group(id);
        }
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹으로 지정할 테이블은 2개 이상이어야 합니다.");
        }
    }

    public void ungroup(final OrderTableValidator orderTableValidator) {
        orderTableValidator.validateAbleToUngroup(this);
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
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
}
